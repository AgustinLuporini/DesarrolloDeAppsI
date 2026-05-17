package com.example.marketlens.components.market

import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock

data class MarketScreenState(
    val isLoading: Boolean = false,
    val cryptos: List<Crypto> = emptyList(),
    val stocks: List<Stock> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)