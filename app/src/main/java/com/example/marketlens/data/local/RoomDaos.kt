package com.example.marketlens.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets WHERE is_crypto = 1")
    fun getCryptosFlow(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE is_crypto = 0")
    fun getStocksFlow(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE is_favorite = 1")
    fun getFavoritesFlow(): Flow<List<AssetEntity>>

    @Query("SELECT id FROM assets WHERE is_favorite = 1")
    suspend fun getFavoritesIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<AssetEntity>)

    @Query("UPDATE assets SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT is_favorite FROM assets WHERE id = :id")
    fun isFavoriteFlow(id: String): Flow<Boolean?>
}

@Dao
interface MacroDao {
    @Query("SELECT * FROM market_indicators")
    fun getAllIndicatorsFlow(): Flow<List<MarketIndicatorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndicators(indicators: List<MarketIndicatorEntity>)
}

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_cache WHERE asset_id = :assetId")
    fun getNewsByAssetFlow(assetId: String): Flow<List<NewsCacheEntity>>

    @Query("SELECT * FROM news_cache WHERE category = 'general'")
    fun getGeneralNewsFlow(): Flow<List<NewsCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsCacheEntity>)

    @Query("DELETE FROM news_cache WHERE asset_id = :assetId")
    suspend fun deleteNewsByAsset(assetId: String)
}

@Dao
interface AiInsightDao {
    @Query("SELECT * FROM ai_insight_cache WHERE asset_id = :assetId LIMIT 1")
    suspend fun getInsight(assetId: String): AiInsightCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: AiInsightCacheEntity)
}
