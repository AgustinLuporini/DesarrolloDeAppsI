package com.example.marketlens.network

import com.example.marketlens.network.responses.MarketNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketApiService {
    @GET("api/v1/news")
    suspend fun getMarketNews(
        @Query("category") category: String = "general",
        @Query("token") apiKey: String
    ): List<MarketNewsResponse>

    @GET("api/v1/company-news")
    suspend fun getCompanyNews(
        @Query("symbol") symbol: String,
        @Query("from") from: String, // Formato YYYY-MM-DD
        @Query("to") to: String,
        @Query("token") apiKey: String
    ): List<MarketNewsResponse>
}