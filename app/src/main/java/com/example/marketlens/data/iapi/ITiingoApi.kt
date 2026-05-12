package com.example.marketlens.data.iapi

import com.example.marketlens.data.results.TiingoResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ITiingoApi {
    @GET("iex/") // IEX es el endpoint para precios en tiempo real (con un poco de delay)
    suspend fun getStockQuotes(
        @Query("tickers") tickers: String,
        @Query("token") apiKey: String
    ): List<TiingoResult>
}