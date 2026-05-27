package com.example.marketlens.data.iapi

import com.example.marketlens.data.results.FredResult
import retrofit2.http.GET
import retrofit2.http.Query

interface IFredApi {
    @GET("fred/series/observations")
    suspend fun getObservation(
        @Query("series_id") seriesId: String,
        @Query("api_key") apiKey: String,
        @Query("file_type") fileType: String = "json"
    ): FredResult
}