package com.example.marketlens.network.api

import com.example.marketlens.network.responses.AssetNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AssetNewsApiService {
    @GET("v1/news/all")
    suspend fun getAssetNews(
        @Query("symbols") symbols: String,
        @Query("api_token") apiKey: String,
        @Query("language") language: String? = null
    ): AssetNewsResponse
}