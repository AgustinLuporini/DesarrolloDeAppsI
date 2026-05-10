package com.example.marketlens.network.api

import com.example.marketlens.network.responses.FredResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FredApiService {
    @GET("fred/series/observations")
    suspend fun getSeriesObservations(
        @Query("series_id") seriesId: String,
        @Query("api_key") apiKey: String,
        @Query("file_type") fileType: String = "json" // para recibir JSON
    ): FredResponse
}