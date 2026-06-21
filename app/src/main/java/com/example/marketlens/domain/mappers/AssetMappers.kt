package com.example.marketlens.domain.mappers

import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.data.results.CoinGeckoResult
import com.example.marketlens.data.results.TiingoResult
import com.example.marketlens.data.local.AssetEntity

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

fun CoinGeckoResult.toEntity(isFavorite: Boolean, timestamp: Long): AssetEntity {
    return AssetEntity(
        id = this.symbol?.uppercase() ?: "???",
        symbol = this.symbol?.uppercase() ?: "???",
        name = this.name ?: "Unknown",
        current_price = this.currentPrice ?: 0.0,
        open_price = this.marketCap ?: 0.0, // map marketCap to open_price
        high_price = this.ath ?: 0.0,        // map ath to high_price
        low_price = 0.0,
        price_change_pct = this.priceChangePercentage24h ?: 0.0,
        image_url = this.image,
        is_crypto = true,
        is_favorite = isFavorite,
        last_updated = timestamp
    )
}

fun TiingoResult.toEntity(isFavorite: Boolean, timestamp: Long): AssetEntity {
    val price = this.lastPrice ?: this.prevClose ?: 0.0
    val pct = if (this.lastPrice != null && this.prevClose != null && this.prevClose != 0.0) {
        ((this.lastPrice - this.prevClose) / this.prevClose) * 100
    } else if (this.prevClose != null && this.open != null && this.open != 0.0) {
        ((this.prevClose - this.open) / this.open) * 100
    } else {
        0.0
    }
    return AssetEntity(
        id = this.ticker ?: "???",
        symbol = this.ticker ?: "???",
        name = this.ticker ?: "Unknown",
        current_price = price,
        open_price = this.open ?: 0.0,
        high_price = this.high ?: 0.0,
        low_price = this.low ?: 0.0,
        price_change_pct = pct,
        image_url = null,
        is_crypto = false,
        is_favorite = isFavorite,
        last_updated = timestamp
    )
}

fun AssetEntity.toCrypto(): Crypto {
    return Crypto(
        ticker = this.symbol,
        name = this.name,
        currentPrice = this.current_price,
        changePercentage = this.price_change_pct,
        imageUrl = this.image_url,
        marketCap = this.open_price, // reconstructed from open_price
        ath = this.high_price       // reconstructed from high_price
    )
}

fun AssetEntity.toStock(): Stock {
    return Stock(
        ticker = this.symbol,
        name = this.name,
        currentPrice = this.current_price,
        changePercentage = this.price_change_pct,
        imageUrl = this.image_url,
        openPrice = this.open_price,
        highPrice = this.high_price
    )
}