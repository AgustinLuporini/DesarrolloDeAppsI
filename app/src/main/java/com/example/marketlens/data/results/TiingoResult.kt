package com.example.marketlens.data.results

import com.google.gson.annotations.SerializedName

data class TiingoResult(
    @SerializedName("ticker") val ticker: String?,
    @SerializedName("last") val lastPrice: Double?, // El 'tngoLast' o 'last'
    @SerializedName("open") val open: Double?,
    @SerializedName("high") val high: Double?,
    @SerializedName("low") val low: Double?,
    @SerializedName("prevClose") val prevClose: Double?,
    @SerializedName("lastSalesTimestamp") val timestamp: String?
)