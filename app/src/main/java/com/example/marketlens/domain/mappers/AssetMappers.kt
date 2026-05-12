package com.example.marketlens.domain.mappers

import com.example.marketlens.domain.models.Asset
import com.example.marketlens.data.results.CoinGeckoResult
import com.example.marketlens.data.results.TiingoResult

fun CoinGeckoResult.toDomain(): Asset.Crypto {
    return Asset.Crypto(
        ticker = this.symbol?.uppercase() ?: "",
        name = this.name ?: "Unknown",
        currentPrice = this.currentPrice ?: 0.0,
        changePercentage = this.priceChangePercentage24h ?: 0.0,
        image = this.image ?: "",
        marketCap = this.marketCap,
        marketCapRank = this.marketCapRank,
        high24h = this.high24h,
        low24h = this.low24h,
        priceChange24h = this.priceChange24h,
        ath = this.ath,
        athChangePercentage = this.athChangePercentage,
        athDate = this.athDate,
        atl = this.atl,
        atlChangePercentage = this.atlChangePercentage,
        atlDate = this.atlDate
    )
}

fun TiingoResult.toDomain(): Asset.Stock {

    val diff = (this.lastPrice ?: 0.0) - (this.prevClose ?: 0.0)
    val pct = if (this.prevClose != null && this.prevClose != 0.0) (diff / this.prevClose) * 100 else 0.0

    return Asset.Stock(
        ticker = this.ticker ?: "",
        name = this.ticker ?: "", // Tiingo no da nombre largo
        currentPrice = this.lastPrice ?: 0.0,
        changePercentage = pct,
        open = this.open,
        high = this.high,
        low = this.low,
        prevClose = this.prevClose,
        timestamp = this.timestamp
    )
}