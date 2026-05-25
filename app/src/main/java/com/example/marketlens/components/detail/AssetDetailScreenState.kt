    package com.example.marketlens.components.detail

    import com.example.marketlens.domain.models.MarketNews

    data class AssetDetailScreenState(
        val isLoading: Boolean = false,
        val ticker: String = "",
        val assetName: String = "",
        val currentPrice: Double = 0.0,
        val changePercentage: Double = 0.0,
        // Datos Dinámicos
        val isCrypto: Boolean = false,
        val openPrice: Double? = null,
        val highPrice: Double? = null,
        val marketCap: Double? = null,
        val ath: Double? = null,
        // Noticias
        val assetNews: List<MarketNews> = emptyList(),
        val finnhubNews: List<MarketNews> = emptyList(),
        val aiSummary: String? = null,
        val error: String? = null
    )