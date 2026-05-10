package com.example.marketlens.network.api

import com.example.marketlens.network.responses.FearGreedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AlternativeApiService {
    @GET("fng/")
    suspend fun getFearAndGreedIndex(
        @Query("limit") limit: Int = 1
    ): FearGreedResponse
}