package com.example.marketlens.domain.repository

interface INewsRepository {
    suspend fun getMarketNews(token: String): List<Any> // Any temporalmente
    suspend fun getAssetSentiment(symbol: String, token: String): Any
}