package com.example.marketlens.data.datasource

import com.example.marketlens.data.iapi.IFredApi
import com.example.marketlens.data.iapi.IAlternativeApi
import com.example.marketlens.data.results.FredResult
import com.example.marketlens.data.results.FearGreedResult
import javax.inject.Inject

class MacroApiDataSource @Inject constructor(
    private val fredApi: IFredApi,
    private val alternativeApi: IAlternativeApi
) {
    suspend fun getFredData(id: String, key: String, limit: Int = 14) = fredApi.getObservation(id, key)
    suspend fun getFearGreed() = alternativeApi.getFearAndGreed()
}