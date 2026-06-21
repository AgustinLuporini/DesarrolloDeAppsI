package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.AssetApiDataSource
import com.example.marketlens.data.local.AssetDao
import com.example.marketlens.domain.mappers.toCrypto
import com.example.marketlens.domain.mappers.toEntity
import com.example.marketlens.domain.mappers.toStock
import com.example.marketlens.domain.models.Asset
import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.domain.repository.IAssetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val dataSource: AssetApiDataSource,
    private val assetDao: AssetDao
) : IAssetRepository {

    override fun getCryptosStream(): Flow<List<Crypto>> {
        return assetDao.getCryptosFlow().map { entities ->
            entities.map { it.toCrypto() }
        }
    }

    override fun getStocksStream(): Flow<List<Stock>> {
        return assetDao.getStocksFlow().map { entities ->
            entities.map { it.toStock() }
        }
    }

    override fun getFavoritesStream(): Flow<List<Asset>> {
        return assetDao.getFavoritesFlow().map { entities ->
            entities.map { entity ->
                if (entity.is_crypto) entity.toCrypto() else entity.toStock()
            }
        }
    }

    override suspend fun refreshCryptos() {
        val response = dataSource.getCryptoMarkets()
        val favorites = assetDao.getFavoritesIds().toSet()
        val timestamp = System.currentTimeMillis()
        val entities = response.map { coin ->
            val symbolUpper = coin.symbol?.uppercase() ?: "???"
            coin.toEntity(isFavorite = favorites.contains(symbolUpper), timestamp = timestamp)
        }
        assetDao.insertAssets(entities)
    }

    override suspend fun refreshStocks(tickers: String, token: String) {
        val response = dataSource.getStockQuotes(tickers, token)
        val favorites = assetDao.getFavoritesIds().toSet()
        val timestamp = System.currentTimeMillis()
        val entities = response.map { stock ->
            val tickerUpper = stock.ticker?.uppercase() ?: "???"
            stock.toEntity(isFavorite = favorites.contains(tickerUpper), timestamp = timestamp)
        }
        assetDao.insertAssets(entities)
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        assetDao.updateFavoriteStatus(id, isFavorite)
    }
}