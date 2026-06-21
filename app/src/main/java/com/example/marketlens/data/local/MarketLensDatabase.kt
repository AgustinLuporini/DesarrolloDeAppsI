package com.example.marketlens.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AssetEntity::class,
        MarketIndicatorEntity::class,
        NewsCacheEntity::class,
        AiInsightCacheEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MarketLensDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
    abstract fun macroDao(): MacroDao
    abstract fun newsDao(): NewsDao
    abstract fun aiInsightDao(): AiInsightDao
}
