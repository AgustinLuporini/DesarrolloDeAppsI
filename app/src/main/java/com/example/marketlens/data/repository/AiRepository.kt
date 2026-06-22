package com.example.marketlens.data.repository

import com.example.marketlens.data.local.AiInsightDao
import com.example.marketlens.domain.mappers.toDomain
import com.example.marketlens.domain.mappers.toEntity
import com.example.marketlens.domain.models.AiInsight
import com.example.marketlens.domain.models.MarketNews
import com.example.marketlens.domain.repository.IAiRepository
import com.example.marketlens.data.network.NetworkConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val aiInsightDao: AiInsightDao
) : IAiRepository {

    private val gson = Gson()

    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-flash-latest",
            apiKey = NetworkConfig.GEMINI_KEY,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            }
        )
    }

    override suspend fun getCachedInsight(ticker: String): AiInsight? {
        return aiInsightDao.getInsight(ticker)?.toDomain()
    }

    override suspend fun generateInsight(ticker: String, newsList: List<MarketNews>): AiInsight? {
        val prompt = if (newsList.isNotEmpty()) {
            buildString {
                append("Analiza las siguientes noticias recientes sobre el activo '$ticker'.\n")
                append("Genera un resumen en español de la tendencia o el sentimiento general del mercado para este activo (máximo 3 frases).\n")
                append("Calcula un puntaje de confianza en la dirección o estabilidad del activo del 0 al 100 basado en el sentimiento de las noticias.\n\n")
                append("Noticias:\n")
                newsList.forEachIndexed { idx, news ->
                    append("${idx + 1}. Titulo: ${news.headline}\n")
                    append("Descripción: ${news.summary}\n")
                    if (news.sentimentScore != null) {
                        append("Sentimiento: ${news.sentimentScore}\n")
                    }
                    append("\n")
                }
                append("\nDevuelve obligatoriamente un objeto JSON con este formato:\n")
                append("{\n  \"summary\": \"Resumen aquí en español\",\n  \"confidence_score\": 80\n}\n")
            }
        } else {
            buildString {
                append("Proporciona una breve perspectiva general del mercado en español para el activo '$ticker' (máximo 3 frases).\n")
                append("Calcula un puntaje de confianza genérico en la estabilidad del activo del 0 al 100 basado en el consenso general del mercado.\n\n")
                append("Devuelve obligatoriamente un objeto JSON con este formato:\n")
                append("{\n  \"summary\": \"Resumen aquí en español\",\n  \"confidence_score\": 70\n}\n")
            }
        }

        return try {
            val response = generativeModel.generateContent(prompt)
            val responseText = response.text ?: throw Exception("Gemini response text is null")
            
            val result = gson.fromJson(responseText, GeminiResponse::class.java)
            val insight = AiInsight(
                assetId = ticker,
                summaryText = result.summary,
                confidenceScore = result.confidence_score,
                timestamp = System.currentTimeMillis()
            )
            
            aiInsightDao.insertInsight(insight.toEntity())
            insight
        } catch (e: Exception) {
            android.util.Log.e("MarketLensTRACK", "Error en generateInsight para $ticker", e)
            null
        }
    }

    private data class GeminiResponse(
        val summary: String,
        val confidence_score: Int
    )
}
