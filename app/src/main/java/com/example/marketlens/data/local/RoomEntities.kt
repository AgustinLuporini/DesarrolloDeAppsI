package com.example.marketlens.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val id: String, // ticker/símbolo (ej: "BTC", "AAPL")
    val symbol: String,
    val name: String,
    val current_price: Double,
    val open_price: Double,
    val high_price: Double,
    val low_price: Double,
    val price_change_pct: Double,
    val image_url: String?,
    val is_crypto: Boolean,
    val is_favorite: Boolean,
    val last_updated: Long
)

@Entity(tableName = "market_indicators")
data class MarketIndicatorEntity(
    @PrimaryKey val id: String, // ID de la serie FRED (ej: "GDP", "FEDFUNDS") o "FNG" para Fear & Greed
    val metric_name: String,
    val value: String,
    val date: String?
)

@Entity(tableName = "news_cache")
data class NewsCacheEntity(
    @PrimaryKey val news_id: String, // ID único de la noticia (ej: de Finnhub o de Stockdata)
    val asset_id: String, // Símbolo del activo (ej: "AAPL") o "general" para la Home
    val title: String,
    val description: String,
    val sentiment_score: Float?,
    val url: String,
    val image_url: String?,
    val category: String
)

@Entity(tableName = "ai_insight_cache")
data class AiInsightCacheEntity(
    @PrimaryKey val asset_id: String, // Ticker del activo
    val summary_text: String,
    val confidence_score: Int,
    val timestamp: Long
)
