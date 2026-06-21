package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.MacroIndicator
import kotlinx.coroutines.flow.Flow

interface IMacroRepository {
    fun getMacroIndicatorsStream(): Flow<List<MacroIndicator>>
    suspend fun refreshMacroIndicators(apiKey: String)
    suspend fun refreshFearGreedIndex()
}