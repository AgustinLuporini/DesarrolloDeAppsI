package com.example.marketlens.data.iapi

import com.example.marketlens.data.results.FearGreedResult
import retrofit2.http.GET
import retrofit2.http.Query

interface IAlternativeApi {
    @GET("fng/")
    suspend fun getFearAndGreed(
        @Query("limit") limit: Int = 1
    ): FearGreedResult
}