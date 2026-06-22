package com.example.marketlens.domain.models

data class AiInsight(
    val assetId: String,
    val summaryText: String,
    val confidenceScore: Int,
    val timestamp: Long
)
