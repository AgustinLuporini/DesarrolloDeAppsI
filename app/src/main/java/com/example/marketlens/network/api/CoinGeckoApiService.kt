package com.example.marketlens.network.api

import com.example.marketlens.network.responses.CoinGeckoMarketItem
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("api/v3/coins/markets")
    suspend fun getCoinMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<CoinGeckoMarketItem>
}