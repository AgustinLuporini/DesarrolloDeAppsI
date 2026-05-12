//Stockdata noticias con sentimiento
package com.example.marketlens.data.iapi

import com.example.marketlens.data.results.AssetNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IAssetNewsApi {
    @GET("v1/news/all")
    suspend fun getAssetNews(
        @Query("symbols") symbols: String,
        @Query("api_token") apiKey: String,
        @Query("language") language: String? = null
    ): AssetNewsResponse
}