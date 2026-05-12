package com.example.marketlens.data.results
import com.google.gson.annotations.SerializedName

data class FredResult(
    @SerializedName("observations") val observations: List<FredObservation>?
)
data class FredObservation(
    @SerializedName("date") val date: String?,
    @SerializedName("value") val value: String?
)