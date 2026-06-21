package com.example.marketlens.di

import com.example.marketlens.data.repository.AssetRepository
import com.example.marketlens.data.repository.MacroRepository
import com.example.marketlens.data.repository.NewsRepository
import com.example.marketlens.domain.repository.IAssetRepository
import com.example.marketlens.domain.repository.IMacroRepository
import com.example.marketlens.domain.repository.INewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAssetRepository(
        assetRepository: AssetRepository
    ): IAssetRepository

    @Binds
    @Singleton
    abstract fun bindMacroRepository(
        macroRepository: MacroRepository
    ): IMacroRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepository: NewsRepository
    ): INewsRepository
}
