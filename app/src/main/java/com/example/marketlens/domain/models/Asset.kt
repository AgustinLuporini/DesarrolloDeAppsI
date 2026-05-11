package com.example.marketlens.domain.models

sealed class Asset {
    abstract val ticker: String
    abstract val name: String
    abstract val currentPrice: Double
    abstract val changePercentage: Double? // Para las acciones hay que calcularlo

    data class Crypto(
        override val ticker: String,
        override val name: String,
        override val currentPrice: Double,
        override val changePercentage: Double,
        val image: String,
        val marketCap: Long?,
        val marketCapRank: Int?,
        val high24h: Double?,
        val low24h: Double?,
        val priceChange24h: Double?,
        val ath: Double?,
        val athChangePercentage: Double?,
        val athDate: String?,
        val atl: Double?,
        val atlChangePercentage: Double?,
        val atlDate: String?
    ) : Asset()

    data class Stock(
        override val ticker: String,
        override val name: String, // Usaremos el ticker como name si la API no lo da
        override val currentPrice: Double,
        override val changePercentage: Double, // La calcularemos en el mapper
        val open: Double?,
        val high: Double?,
        val low: Double?,
        val prevClose: Double?,
        val timestamp: String?
    ) : Asset()
}