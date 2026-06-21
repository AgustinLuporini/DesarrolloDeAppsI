package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.NewsApiDataSource
import com.example.marketlens.data.local.NewsDao
import com.example.marketlens.domain.mappers.toDomain
import com.example.marketlens.domain.mappers.toEntity
import com.example.marketlens.domain.models.MarketNews
import com.example.marketlens.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val dataSource: NewsApiDataSource,
    private val newsDao: NewsDao
) : INewsRepository {

    override fun getGeneralNewsStream(): Flow<List<MarketNews>> {
        return newsDao.getGeneralNewsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNewsByAssetStream(assetId: String): Flow<List<MarketNews>> {
        return newsDao.getNewsByAssetFlow(assetId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshGeneralNews(token: String) {
        val response = dataSource.getGeneralNews(token)
        val entities = response.map { it.toEntity(assetId = "general", category = "general") }
        newsDao.insertNews(entities)
    }

    override suspend fun refreshAssetNews(ticker: String, from: String, to: String, apiKey: String) {
        try {
            val response = dataSource.getAssetNews(
                symbol = ticker,
                from = from,
                to = to,
                token = apiKey
            )
            val entities = response.map { it.toEntity(assetId = ticker, category = "corporate") }
            newsDao.insertNews(entities)
        } catch (e: Exception) {
            // silent fail
        }
    }

    override suspend fun refreshAssetSentiment(symbol: String, token: String) {
        try {
            val response = dataSource.getSentimentNews(symbol, token)
            val entities = response.data?.map { it.toEntity(assetId = symbol, category = "sentiment") } ?: emptyList()
            newsDao.insertNews(entities)
        } catch (e: Exception) {
            // silent fail
        }
    }
}