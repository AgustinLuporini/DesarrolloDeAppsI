package com.example.marketlens.domain.models

abstract class Asset {
    abstract val ticker: String
    abstract val name: String
    abstract val currentPrice: Double
    abstract val changePercentage: Double
    abstract val imageUrl: String?
}

data class Crypto(
    override val ticker: String,
    override val name: String,
    override val currentPrice: Double,
    override val changePercentage: Double,
    override val imageUrl: String?,
    val marketCap: Double,
    val ath: Double
) : Asset()

data class Stock(
    override val ticker: String,
    override val name: String,
    override val currentPrice: Double,
    override val changePercentage: Double,
    override val imageUrl: String?,
    val openPrice: Double,
    val highPrice: Double
) : Asset()