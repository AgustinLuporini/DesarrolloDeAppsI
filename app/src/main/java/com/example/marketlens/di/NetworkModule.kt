package com.example.marketlens.di

import com.example.marketlens.data.iapi.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinGeckoApi(): ICoinGeckoApi {
        return buildRetrofit("https://api.coingecko.com/").create(ICoinGeckoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTiingoApi(): ITiingoApi {
        return buildRetrofit("https://api.tiingo.com/").create(ITiingoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarketApi(): IMarketApi {
        return buildRetrofit("https://finnhub.io/").create(IMarketApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAssetNewsApi(): IAssetNewsApi {
        return buildRetrofit("https://api.stockdata.org/").create(IAssetNewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFredApi(): IFredApi {
        return buildRetrofit("https://api.stlouisfed.org/").create(IFredApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAlternativeApi(): IAlternativeApi {
        return buildRetrofit("https://api.alternative.me/").create(IAlternativeApi::class.java)
    }
}
