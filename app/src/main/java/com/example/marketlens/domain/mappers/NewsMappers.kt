package com.example.marketlens.domain.mappers

import com.example.marketlens.data.results.*
import com.example.marketlens.domain.models.*

// Finnhub --> MarketNews
fun MarketNewsResult.toDomain() = MarketNews(
    id = this.id.toString(),
    headline = this.headline ?: "",
    summary = this.summary ?: "",
    url = this.url ?: "",
    source = this.source ?: "",
    datetime = this.datetime ?: 0L
)


fun AssetNewsItem.toDomain(): MarketNews {
    val score = this.entities?.firstOrNull()?.sentimentScore
    return MarketNews(
        id = this.uuid ?: "",
        headline = this.title ?: "Sin título",
        summary = this.description ?: "",
        url = this.url ?: "",
        source = this.source ?: "Unknown",
        datetime = 0L,
        sentimentScore = score
    )
}