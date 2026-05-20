package com.example.marketlens.domain.mappers

import com.example.marketlens.data.results.FearGreedResult
import com.example.marketlens.data.results.FredResult
import com.example.marketlens.domain.models.*

fun FearGreedResult.toDomain(): MacroIndicator {
    val data = this.data?.firstOrNull()
    return MacroIndicator(
        value = data?.value ?: "0",
        description = data?.classification ?: "Unknown"
    )
}

fun FredResult.toDomain(seriesName: String): MacroIndicator {
    val data = this.observations?.lastOrNull { it.value != "." }
    return MacroIndicator(
        value = data?.value ?: "0",
        description = seriesName
    )
}