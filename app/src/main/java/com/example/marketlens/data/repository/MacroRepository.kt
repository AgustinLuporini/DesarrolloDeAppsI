package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.MacroApiDataSource
import com.example.marketlens.data.local.MacroDao
import com.example.marketlens.domain.mappers.toDomain
import com.example.marketlens.domain.mappers.toEntity
import com.example.marketlens.domain.models.MacroIndicator
import com.example.marketlens.domain.repository.IMacroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MacroRepository @Inject constructor(
    private val dataSource: MacroApiDataSource,
    private val macroDao: MacroDao
) : IMacroRepository {

    override fun getMacroIndicatorsStream(): Flow<List<MacroIndicator>> {
        return macroDao.getAllIndicatorsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshMacroIndicators(apiKey: String) {
        val ids = listOf(
            "GDP" to "PBI (USA)",
            "CPIAUCSL" to "Inflación (CPI)",
            "FEDFUNDS" to "Tasa de Interés",
            "UNRATE" to "Tasa de Desempleo"
        )

        val entities = ids.map { (id, label) ->
            val result = dataSource.getFredData(id, apiKey, limit = 14)
            result.toEntity(id, label)
        }
        macroDao.insertIndicators(entities)
    }

    override suspend fun refreshFearGreedIndex() {
        val result = dataSource.getFearGreed()
        macroDao.insertIndicators(listOf(result.toEntity()))
    }
}