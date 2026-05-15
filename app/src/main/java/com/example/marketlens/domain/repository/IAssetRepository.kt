package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock

interface IAssetRepository {
    suspend fun getCryptos(): List<Crypto>
    suspend fun getStocks(tickers: String, token: String): List<Stock>
}