package com.example.marketlens.data.datasource

import com.example.marketlens.data.iapi.ICoinGeckoApi
import com.example.marketlens.data.iapi.ITiingoApi
import com.example.marketlens.data.results.CoinGeckoResult
import com.example.marketlens.data.results.TiingoResult

import javax.inject.Inject

class AssetApiDataSource @Inject constructor(
    private val coinGeckoApi: ICoinGeckoApi,
    private val tiingoApi: ITiingoApi
) {
    suspend fun getCryptoMarkets(): List<CoinGeckoResult> {
        return coinGeckoApi.getCoinMarkets()
    }
    suspend fun getStockQuotes(tickers: String, token: String): List<TiingoResult> {
        return tiingoApi.getStockQuotes(tickers, token)
    }
}