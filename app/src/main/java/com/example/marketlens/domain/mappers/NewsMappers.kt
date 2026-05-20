package com.example.marketlens.domain.mappers

import com.example.marketlens.data.results.*
import com.example.marketlens.domain.models.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Finnhub --> MarketNews
fun MarketNewsResult.toDomain(): MarketNews {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateString = if (this.datetime != null) sdf.format(Date(this.datetime * 1000)) else ""

    return MarketNews(
        id = this.id.toString(),
        headline = this.headline ?: "",
        summary = this.summary ?: "",
        url = this.url ?: "",
        imageUrl = this.image ?: "",
        source = this.source ?: "",
        datetime = this.datetime ?: 0L,
        date = dateString
    )
}

// Stockdata --> MarketNews
fun AssetNewsItem.toDomain(): MarketNews {
    val score = this.entities?.firstOrNull()?.sentimentScore
    return MarketNews(
        id = this.uuid ?: "",
        headline = this.title ?: "Sin título",
        summary = this.description ?: "",
        url = this.url ?: "",
        imageUrl = this.imageUrl ?: "",
        source = this.source ?: "Unknown",
        datetime = 0L,
        date = this.publishedAt?.substringBefore("T") ?: "",
        sentimentScore = score
    )
}