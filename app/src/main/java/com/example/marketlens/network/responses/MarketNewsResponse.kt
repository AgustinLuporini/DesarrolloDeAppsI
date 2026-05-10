package com.example.marketlens.network.responses
import com.google.gson.annotations.SerializedName

data class MarketNewsResponse (
    @SerializedName("category") val category: String?,
    @SerializedName("datetime") val datetime: Long?,
    @SerializedName("headline") val headline: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("image") val image: String?,
    @SerializedName("related") val related: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("url") val url: String?
)

