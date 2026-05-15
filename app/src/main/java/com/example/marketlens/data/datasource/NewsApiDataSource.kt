package com.example.marketlens.data.datasource

import com.example.marketlens.data.RetrofitInstance

class NewsApiDataSource {
    private val marketApi = RetrofitInstance.marketNewsService
    private val assetApi = RetrofitInstance.assetNewsService

    suspend fun getGeneralNews(token: String) = marketApi.getMarketNews(apiKey = token)

    suspend fun getSentimentNews(symbol: String, token: String) = assetApi.getAssetNews(symbol, token)
}