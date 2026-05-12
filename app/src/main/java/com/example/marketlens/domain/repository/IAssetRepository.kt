package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.Asset

interface IAssetRepository {
    suspend fun getCryptos(): List<Asset.Crypto>
    suspend fun getStocks(tickers: String, token: String): List<Asset.Stock>
}