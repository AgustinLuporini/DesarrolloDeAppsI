package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.NewsApiDataSource
import com.example.marketlens.domain.models.MarketNews
import com.example.marketlens.domain.repository.INewsRepository
import com.example.marketlens.domain.mappers.toDomain
import com.example.marketlens.data.RetrofitInstance

class NewsRepository : INewsRepository {
    private val dataSource = NewsApiDataSource()

    override suspend fun getMarketNews(token: String): List<MarketNews> {
        val response = dataSource.getGeneralNews(token)
        return response.map { it.toDomain() }
    }

    override suspend fun getAssetSentiment(symbol: String, token: String): List<MarketNews> {
        val response = dataSource.getSentimentNews(symbol, token)
        return response.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getAssetNews(ticker: String, from: String, to: String, apiKey: String): List<MarketNews> {
        return try {
            val response = dataSource.getAssetNews(
                symbol = ticker,
                from = from,
                to = to,
                token = apiKey
            )

            response.map { it.toDomain() }
        } catch (e: Exception) {

            emptyList()
        }
    }
}