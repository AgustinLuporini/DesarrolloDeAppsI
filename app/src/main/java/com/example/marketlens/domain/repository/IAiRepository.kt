package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.AiInsight
import com.example.marketlens.domain.models.MarketNews

interface IAiRepository {
    suspend fun getCachedInsight(ticker: String): AiInsight?
    suspend fun generateInsight(ticker: String, newsList: List<MarketNews>): AiInsight?
}
