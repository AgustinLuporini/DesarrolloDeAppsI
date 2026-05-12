package com.example.marketlens.data.datasource

import com.example.marketlens.data.iapi.IFredApi
import com.example.marketlens.data.iapi.IAlternativeApi
import com.example.marketlens.data.results.FredResult
import com.example.marketlens.data.results.FearGreedResult


class MacroApiDataSource(
    private val fredApi: IFredApi,
    private val alternativeApi: IAlternativeApi
) {
    // PBI, inflacion, tasa de interes, o desempleo
    suspend fun getEconomicIndicator(seriesId: String, apiKey: String): FredResult {
        return fredApi.getObservation(seriesId, apiKey)
    }

    // Trae el índice de Miedo y Codicia (Fear & Greed)
    suspend fun getFearAndGreedIndex(): FearGreedResult {
        return alternativeApi.getFearAndGreed()
    }
}