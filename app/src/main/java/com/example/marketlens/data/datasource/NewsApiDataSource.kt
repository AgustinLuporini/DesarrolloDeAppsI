package com.example.marketlens.data.datasource

import com.example.marketlens.data.iapi.IMarketApi
import com.example.marketlens.data.iapi.IAssetNewsApi
import javax.inject.Inject

class NewsApiDataSource @Inject constructor(
    private val marketApi: IMarketApi,
    private val assetApi: IAssetNewsApi
) {
    suspend fun getGeneralNews(token: String) = marketApi.getMarketNews(apiKey = token)

    suspend fun getSentimentNews(symbol: String, token: String) = assetApi.getAssetNews(symbol, token)
    suspend fun getAssetNews(symbol: String, from: String, to: String, token: String) =
        marketApi.getAssetNews(symbol, from, to, token)
}