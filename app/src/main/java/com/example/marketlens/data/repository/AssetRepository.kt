package com.example.marketlens.data.repository

import com.example.marketlens.data.RetrofitInstance
import com.example.marketlens.domain.models.Asset
import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.domain.repository.IAssetRepository
import com.example.marketlens.domain.mappers.toDomain

class AssetRepository : IAssetRepository {
    private val coinGeckoApi = RetrofitInstance.coinGeckoService
    private val tiingoApi = RetrofitInstance.tiingoService

    override suspend fun getCryptos(): List<Crypto> {
        val response = coinGeckoApi.getCoinMarkets()
        return response.map { it.toDomain() }
    }

    override suspend fun getStocks(tickers: String, token: String): List<Stock> {
        val response = tiingoApi.getStockQuotes(tickers, token)
        return response.map { it.toDomain() }
    }
}