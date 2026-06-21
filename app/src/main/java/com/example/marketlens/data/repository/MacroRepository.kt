package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.MacroApiDataSource
import com.example.marketlens.domain.models.MacroIndicator
import com.example.marketlens.domain.repository.IMacroRepository
import com.example.marketlens.domain.mappers.toDomain
import javax.inject.Inject

class MacroRepository @Inject constructor(
    private val dataSource: MacroApiDataSource
) : IMacroRepository {

    override suspend fun getFearGreedIndex(): MacroIndicator {
        val result = dataSource.getFearGreed()
        return result.toDomain()
    }

    override suspend fun getEconomicIndex(seriesId: String, apiKey: String): MacroIndicator {
        val result = dataSource.getFredData(seriesId, apiKey)


        val label = when (seriesId) {
            "GDP" -> "PBI (USA)"
            "CPIAUCSL" -> "Inflación (CPI)"
            "FEDFUNDS" ->  "Tasa de interes"
            "UNRATE" -> "Tasa de desempleo"
            else -> "Indicador Económico"
        }

        return result.toDomain(label)
    }

    override suspend fun getAllMacroIndicators(apiKey: String): List<MacroIndicator> {
        val ids = listOf(
            "GDP" to "PBI (USA)",
            "CPIAUCSL" to "Inflación (CPI)",
            "FEDFUNDS" to "Tasa de Interés",
            "UNRATE" to "Tasa de Desempleo"
        )

        return ids.map { (id, label) ->
            val result = dataSource.getFredData(id, apiKey, limit = 14)
            result.toDomain(label)
        }
    }
}