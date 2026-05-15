package com.example.marketlens.data.datasource

import com.example.marketlens.data.RetrofitInstance
import com.example.marketlens.data.iapi.IFredApi
import com.example.marketlens.data.iapi.IAlternativeApi
import com.example.marketlens.data.results.FredResult
import com.example.marketlens.data.results.FearGreedResult


class MacroApiDataSource {
    private val fredApi = RetrofitInstance.fredService
    private val alternativeApi = RetrofitInstance.alternativeService

    suspend fun getFredData(id: String, key: String) = fredApi.getObservation(id, key)
    suspend fun getFearGreed() = alternativeApi.getFearAndGreed()
}