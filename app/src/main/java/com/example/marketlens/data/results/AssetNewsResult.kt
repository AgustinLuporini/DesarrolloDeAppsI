package com.example.marketlens.data.results

import com.google.gson.annotations.SerializedName

data class AssetNewsResponse(
    @SerializedName("data") val data: List<AssetNewsItem>?
)
data class AssetNewsItem(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("entities") val entities: List<AssetSentiment>?
)
data class AssetSentiment(
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("sentiment_score") val sentimentScore: Double?,
    @SerializedName("match_score") val matchScore: Double?
)