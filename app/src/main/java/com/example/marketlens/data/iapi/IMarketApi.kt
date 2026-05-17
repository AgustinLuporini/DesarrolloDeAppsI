//Finnhub noticias tradicionales
package com.example.marketlens.data.iapi

import com.example.marketlens.data.results.MarketNewsResult
import retrofit2.http.GET
import retrofit2.http.Query

interface IMarketApi {
    @GET("api/v1/news")
    suspend fun getMarketNews(
        @Query("category") category: String = "general",
        @Query("token") apiKey: String
    ): List<MarketNewsResult>

    @GET("api/v1/company-news")
    suspend fun getAssetNews(
        @Query("symbol") symbol: String,
        @Query("from") from: String, // Formato YYYY-MM-DD
        @Query("to") to: String,
        @Query("token") apiKey: String
    ): List<MarketNewsResult>


}