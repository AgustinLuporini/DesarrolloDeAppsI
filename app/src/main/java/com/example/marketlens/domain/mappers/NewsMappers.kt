package com.example.marketlens.domain.mappers

import com.example.marketlens.data.results.*
import com.example.marketlens.domain.models.*
import com.example.marketlens.data.local.NewsCacheEntity
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

// Finnhub --> NewsCacheEntity
fun MarketNewsResult.toEntity(assetId: String, category: String): NewsCacheEntity {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateString = if (this.datetime != null) sdf.format(Date(this.datetime * 1000)) else ""
    val src = this.source ?: "Finnhub"
    
    // Almacenamos el origen y la fecha dentro de la descripción para respetar las columnas exactas del diccionario
    val packedDescription = "[$src | $dateString] ${this.summary ?: ""}"

    return NewsCacheEntity(
        news_id = this.id.toString(),
        asset_id = assetId,
        title = this.headline ?: "",
        description = packedDescription,
        sentiment_score = null,
        url = this.url ?: "",
        image_url = this.image,
        category = category
    )
}

// Stockdata --> NewsCacheEntity
fun AssetNewsItem.toEntity(assetId: String, category: String): NewsCacheEntity {
    val score = this.entities?.firstOrNull()?.sentimentScore?.toFloat()
    val dateString = this.publishedAt?.substringBefore("T") ?: ""
    val src = this.source ?: "StockData"
    val packedDescription = "[$src | $dateString] ${this.description ?: ""}"

    return NewsCacheEntity(
        news_id = this.uuid ?: "",
        asset_id = assetId,
        title = this.title ?: "Sin título",
        description = packedDescription,
        sentiment_score = score,
        url = this.url ?: "",
        image_url = this.imageUrl,
        category = category
    )
}

// NewsCacheEntity --> MarketNews
fun NewsCacheEntity.toDomain(): MarketNews {
    // Extraer source y date de la descripción empaquetada
    val regex = java.util.regex.Pattern.compile("^\\[(.*?)\\|(.*?)\\] (.*)$")
    val matcher = regex.matcher(this.description)
    
    var source = "MarketLens"
    var date = ""
    var summary = this.description

    if (matcher.find()) {
        source = matcher.group(1)?.trim() ?: "MarketLens"
        date = matcher.group(2)?.trim() ?: ""
        summary = matcher.group(3) ?: this.description
    }

    return MarketNews(
        id = this.news_id,
        headline = this.title,
        summary = summary,
        url = this.url,
        imageUrl = this.image_url ?: "",
        source = source,
        datetime = 0L,
        date = date,
        sentimentScore = this.sentiment_score?.toDouble()
    )
}