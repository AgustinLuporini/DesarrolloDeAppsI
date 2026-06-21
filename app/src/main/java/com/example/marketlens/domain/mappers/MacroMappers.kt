package com.example.marketlens.domain.mappers

import com.example.marketlens.data.results.FearGreedResult
import com.example.marketlens.data.results.FredResult
import com.example.marketlens.domain.models.*
import com.example.marketlens.data.local.MarketIndicatorEntity

fun FearGreedResult.toDomain(): MacroIndicator {
    val data = this.data?.firstOrNull()
    return MacroIndicator(
        id = "FNG",
        value = data?.value ?: "0",
        description = data?.classification ?: "Unknown"
    )
}

fun FredResult.toDomain(seriesName: String, seriesId: String? = null): MacroIndicator {
    val obs = this.observations?.filter { it.value != null && it.value != "." } ?: emptyList()

    if (obs.isEmpty()) return MacroIndicator(id = seriesId, value = "N/A", description = seriesName)

    val valorActual = obs.last().value?.toDoubleOrNull() ?: 0.0

    val valorFinal = if (seriesName == "Inflación (CPI)" && obs.size >= 13) {
        val valorHaceUnAnio = obs[obs.size - 13].value?.toDoubleOrNull() ?: 1.0
        val inflacionYoY = ((valorActual / valorHaceUnAnio) - 1) * 100
        "%.2f%%".format(inflacionYoY)
    } else {
        "%.2f".format(valorActual)
    }

    return MacroIndicator(
        id = seriesId,
        value = valorFinal,
        description = seriesName
    )
}

fun MarketIndicatorEntity.toDomain(): MacroIndicator {
    return MacroIndicator(
        id = this.id,
        value = this.value,
        description = this.metric_name,
        date = this.date
    )
}

fun FearGreedResult.toEntity(): MarketIndicatorEntity {
    val data = this.data?.firstOrNull()
    return MarketIndicatorEntity(
        id = "FNG",
        metric_name = data?.classification ?: "Unknown",
        value = data?.value ?: "0",
        date = data?.timestamp
    )
}

fun FredResult.toEntity(seriesId: String, seriesName: String): MarketIndicatorEntity {
    val obs = this.observations?.filter { it.value != null && it.value != "." } ?: emptyList()
    if (obs.isEmpty()) return MarketIndicatorEntity(seriesId, seriesName, "N/A", null)

    val valorActual = obs.last().value?.toDoubleOrNull() ?: 0.0
    val valorFinal = if (seriesName == "Inflación (CPI)" && obs.size >= 13) {
        val valorHaceUnAnio = obs[obs.size - 13].value?.toDoubleOrNull() ?: 1.0
        val inflacionYoY = ((valorActual / valorHaceUnAnio) - 1) * 100
        "%.2f%%".format(inflacionYoY)
    } else {
        "%.2f".format(valorActual)
    }

    return MarketIndicatorEntity(
        id = seriesId,
        metric_name = seriesName,
        value = valorFinal,
        date = obs.last().date
    )
}