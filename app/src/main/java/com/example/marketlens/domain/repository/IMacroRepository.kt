package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.MacroIndicator

interface IMacroRepository {
    suspend fun getEconomicIndex(seriesId: String, token: String): MacroIndicator
    suspend fun getFearGreedIndex(): MacroIndicator
    suspend fun getAllMacroIndicators(apiKey: String): List<MacroIndicator>
}