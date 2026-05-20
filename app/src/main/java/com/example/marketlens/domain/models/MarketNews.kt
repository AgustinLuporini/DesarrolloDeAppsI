package com.example.marketlens.domain.models

data class MarketNews(
    val id: String,
    val headline: String,
    val summary: String,
    val url: String,
    val source: String,
    val datetime: Long,
    val sentimentScore: Double? = null, // Solo lo tiene Stockdata
    val imageUrl: String,
    val date: String
)