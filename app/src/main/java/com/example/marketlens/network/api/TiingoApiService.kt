package com.example.marketlens.network.api

import com.example.marketlens.network.responses.TiingoStockItem
import retrofit2.http.GET
import retrofit2.http.Query

interface TiingoApiService {
    @GET("iex/") // IEX es el endpoint para precios en tiempo real (con un poco de delay)
    suspend fun getStockQuotes(
        @Query("tickers") tickers: String,
        @Query("token") apiKey: String
    ): List<TiingoStockItem>
}