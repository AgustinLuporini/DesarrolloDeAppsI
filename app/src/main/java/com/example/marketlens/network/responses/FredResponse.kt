package com.example.marketlens.network.responses
import com.google.gson.annotations.SerializedName

data class FredResponse(
    @SerializedName("observations") val observations: List<FredObservation>?
)
data class FredObservation(
    @SerializedName("date") val date: String?,
    @SerializedName("value") val value: String?
)