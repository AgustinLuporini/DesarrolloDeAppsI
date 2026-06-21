package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.MarketNews
import kotlinx.coroutines.flow.Flow

interface INewsRepository {
    fun getGeneralNewsStream(): Flow<List<MarketNews>>
    fun getNewsByAssetStream(assetId: String): Flow<List<MarketNews>>
    suspend fun refreshGeneralNews(token: String)
    suspend fun refreshAssetNews(ticker: String, from: String, to: String, apiKey: String)
    suspend fun refreshAssetSentiment(symbol: String, token: String)
}