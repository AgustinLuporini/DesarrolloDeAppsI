package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.AssetApiDataSource
import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.domain.repository.IAssetRepository
import com.example.marketlens.domain.mappers.toDomain
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val dataSource: AssetApiDataSource
) : IAssetRepository {

    override suspend fun getCryptos(): List<Crypto> {
        val response = dataSource.getCryptoMarkets()
        return response.map { it.toDomain() }
    }

    override suspend fun getStocks(tickers: String, token: String): List<Stock> {
        val response = dataSource.getStockQuotes(tickers, token)
        return response.map { it.toDomain() }
    }
}