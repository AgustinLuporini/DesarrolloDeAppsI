package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.MarketNews

interface INewsRepository {
    suspend fun getMarketNews(token: String): List<MarketNews>
    suspend fun getAssetSentiment(symbol: String, token: String): List<MarketNews>
    suspend fun getAssetNews (ticker: String, from: String, to: String, apiKey: String): List<MarketNews>
}