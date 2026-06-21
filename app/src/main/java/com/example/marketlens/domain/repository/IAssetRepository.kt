package com.example.marketlens.domain.repository

import com.example.marketlens.domain.models.Asset
import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import kotlinx.coroutines.flow.Flow

interface IAssetRepository {
    fun getCryptosStream(): Flow<List<Crypto>>
    fun getStocksStream(): Flow<List<Stock>>
    fun getFavoritesStream(): Flow<List<Asset>>
    suspend fun refreshCryptos()
    suspend fun refreshStocks(tickers: String, token: String)
    suspend fun toggleFavorite(id: String, isFavorite: Boolean)
    fun isFavoriteStream(id: String): Flow<Boolean>
    suspend fun syncUserSession(firebaseUser: com.google.firebase.auth.FirebaseUser)
}