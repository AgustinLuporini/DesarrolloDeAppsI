package com.example.marketlens.domain.mappers

import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.data.results.CoinGeckoResult
import com.example.marketlens.data.results.TiingoResult

fun CoinGeckoResult.toDomain(): Crypto {
    return Crypto(
        ticker = this.symbol?.uppercase() ?: "???",
        name = this.name ?: "Unknown",
        currentPrice = this.currentPrice ?: 0.0,
        changePercentage = this.priceChangePercentage24h ?: 0.0,
        imageUrl = this.image,
        marketCap = this.marketCap ?: 0.0,
        ath = this.ath ?: 0.0
    )
}

fun TiingoResult.toDomain(): Stock {
    val diff = (this.lastPrice ?: 0.0) - (this.prevClose ?: 0.0)
    val pct = if (this.prevClose != null && this.prevClose != 0.0) (diff / this.prevClose) * 100 else 0.0

    return Stock(
        ticker = this.ticker ?: "???",
        name = this.ticker ?: "Unknown", // Tiingo no da el nombre largo en este endpoint
        currentPrice = this.lastPrice ?: 0.0,
        changePercentage = pct,
        imageUrl = null, // Tiingo da imagen del logo
        openPrice = this.open ?: 0.0,
        highPrice = this.high ?: 0.0
    )
}