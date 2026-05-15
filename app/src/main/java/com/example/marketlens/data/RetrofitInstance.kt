package com.example.marketlens.data

import com.example.marketlens.data.iapi.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val coinGeckoService: ICoinGeckoApi by lazy {
        buildRetrofit("https://api.coingecko.com/").create(ICoinGeckoApi::class.java)
}
    val tiingoService: ITiingoApi by lazy {
        buildRetrofit("https://api.tiingo.com/").create(ITiingoApi::class.java)
    }
    val marketNewsService: IMarketApi by lazy {
        buildRetrofit("https://finnhub.io/").create(IMarketApi::class.java)
    }
    val assetNewsService: IAssetNewsApi by lazy {
        buildRetrofit("https://api.stockdata.org/").create(IAssetNewsApi::class.java)
    }
    val fredService: IFredApi by lazy {
        buildRetrofit("https://api.stlouisfed.org/").create(IFredApi::class.java)
    }
    val alternativeService: IAlternativeApi by lazy {
        buildRetrofit("https://api.alternative.me/").create(IAlternativeApi::class.java)
    }
}