package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.AssetApiDataSource
import com.example.marketlens.data.local.AssetDao
import com.example.marketlens.data.local.AssetEntity
import com.example.marketlens.domain.mappers.toCrypto
import com.example.marketlens.domain.mappers.toEntity
import com.example.marketlens.domain.mappers.toStock
import com.example.marketlens.domain.models.Asset
import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.domain.repository.IAssetRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val dataSource: AssetApiDataSource,
    private val assetDao: AssetDao,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
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

    override fun isFavoriteStream(id: String): Flow<Boolean> {
        return assetDao.isFavoriteFlow(id).map { it ?: false }
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

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val docRef = firestore.collection("user_watchlist").document(currentUser.uid)
            try {
                if (isFavorite) {
                    docRef.update("favorites", FieldValue.arrayUnion(id)).await()
                } else {
                    docRef.update("favorites", FieldValue.arrayRemove(id)).await()
                }
            } catch (e: Exception) {
                if (isFavorite) {
                    docRef.set(mapOf("favorites" to listOf(id))).await()
                } else {
                    docRef.set(mapOf("favorites" to emptyList<String>())).await()
                }
            }
        }
    }

    override suspend fun syncUserSession(firebaseUser: FirebaseUser) {
        val uid = firebaseUser.uid
        val email = firebaseUser.email ?: ""
        val displayName = firebaseUser.displayName ?: ""
        val photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        val timestamp = System.currentTimeMillis()

        val userMap = mapOf(
            "uid" to uid,
            "email" to email,
            "displayName" to displayName,
            "photoUrl" to photoUrl,
            "lastLogin" to timestamp
        )
        firestore.collection("users").document(uid).set(userMap).await()

        val watchlistDoc = firestore.collection("user_watchlist").document(uid).get().await()
        @Suppress("UNCHECKED_CAST")
        val remoteFavorites = (watchlistDoc.get("favorites") as? List<String>)?.toSet() ?: emptySet()

        val localFavorites = assetDao.getFavoritesIds().toSet()

        val combinedFavorites = localFavorites + remoteFavorites

        for (ticker in combinedFavorites) {
            assetDao.updateFavoriteStatus(ticker, true)
            val isFavoriteLocal = localFavorites.contains(ticker)
            if (!isFavoriteLocal) {
                val skeleton = AssetEntity(
                    id = ticker,
                    symbol = ticker,
                    name = "Loading...",
                    current_price = 0.0,
                    open_price = 0.0,
                    high_price = 0.0,
                    low_price = 0.0,
                    price_change_pct = 0.0,
                    image_url = null,
                    is_crypto = false,
                    is_favorite = true,
                    last_updated = timestamp
                )
                assetDao.insertAssets(listOf(skeleton))
            }
        }

        if (combinedFavorites.isNotEmpty()) {
            firestore.collection("user_watchlist").document(uid)
                .set(mapOf("favorites" to combinedFavorites.toList()))
                .await()
        }
    }
}