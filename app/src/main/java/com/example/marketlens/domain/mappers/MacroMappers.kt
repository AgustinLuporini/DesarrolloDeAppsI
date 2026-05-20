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

// MacroMappers.kt corregido
fun FredResult.toDomain(seriesName: String): MacroIndicator {
    // Filtro para nulos y .
    val obs = this.observations?.filter { it.value != null && it.value != "." } ?: emptyList()

    if (obs.isEmpty()) return MacroIndicator("N/A", seriesName)

    val valorActual = obs.last().value?.toDoubleOrNull() ?: 0.0

    val valorFinal = if (seriesName == "Inflación (CPI)" && obs.size >= 13) {
        // Acceder al valor solo si no es nulo
        val valorHaceUnAnio = obs[obs.size - 13].value?.toDoubleOrNull() ?: 1.0
        val inflacionYoY = ((valorActual / valorHaceUnAnio) - 1) * 100
        "%.2f%%".format(inflacionYoY)
    } else {
        // Formateo para que el número se vea limpio
        "%.2f".format(valorActual)
    }

    return MacroIndicator(
        value = valorFinal,
        description = seriesName
    )
}