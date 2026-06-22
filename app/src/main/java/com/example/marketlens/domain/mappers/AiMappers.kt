package com.example.marketlens.domain.mappers

import com.example.marketlens.data.local.AiInsightCacheEntity
import com.example.marketlens.domain.models.AiInsight

fun AiInsightCacheEntity.toDomain() = AiInsight(
    assetId = asset_id,
    summaryText = summary_text,
    confidenceScore = confidence_score,
    timestamp = timestamp
)

fun AiInsight.toEntity() = AiInsightCacheEntity(
    asset_id = assetId,
    summary_text = summaryText,
    confidence_score = confidenceScore,
    timestamp = timestamp
)
