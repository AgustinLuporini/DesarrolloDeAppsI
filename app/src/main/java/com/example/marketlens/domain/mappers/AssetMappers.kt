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
    val price = this.lastPrice ?: this.prevClose ?: 0.0

    val pct = if (this.lastPrice != null && this.prevClose != null && this.prevClose != 0.0) {
        ((this.lastPrice - this.prevClose) / this.prevClose) * 100
    } else if (this.prevClose != null && this.open != null && this.open != 0.0) {
        ((this.prevClose - this.open) / this.open) * 100
    } else {
        0.0
    }

    return Stock(
        ticker = this.ticker ?: "???",
        name = this.ticker ?: "Unknown",
        currentPrice = price,
        changePercentage = pct,
        imageUrl = null,
        openPrice = this.open ?: 0.0,
        highPrice = this.high ?: 0.0
    )
}