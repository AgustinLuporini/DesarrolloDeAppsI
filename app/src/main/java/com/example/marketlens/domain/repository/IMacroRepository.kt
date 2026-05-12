package com.example.marketlens.domain.repository

interface IMacroRepository {
    suspend fun getEconomicIndex(seriesId: String, token: String): String
    suspend fun getFearGreedIndex(): String
}