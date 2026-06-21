package com.example.marketlens.di

import android.content.Context
import androidx.room.Room
import com.example.marketlens.data.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MarketLensDatabase {
        return Room.databaseBuilder(
            context,
            MarketLensDatabase::class.java,
            "marketlens_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideAssetDao(db: MarketLensDatabase): AssetDao = db.assetDao()

    @Provides
    fun provideMacroDao(db: MarketLensDatabase): MacroDao = db.macroDao()

    @Provides
    fun provideNewsDao(db: MarketLensDatabase): NewsDao = db.newsDao()

    @Provides
    fun provideAiInsightDao(db: MarketLensDatabase): AiInsightDao = db.aiInsightDao()
}
