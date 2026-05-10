package com.example.marketlens.network.responses

import com.google.gson.annotations.SerializedName

data class FearGreedResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("data") val data: List<FearGreedData>?
)

data class FearGreedData(
    @SerializedName("value") val value: String?,
    @SerializedName("value_classification") val classification: String?,
    @SerializedName("timestamp") val timestamp: String?
)