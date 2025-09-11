package com.simplifynow.cryptowatcher.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton to instantiate the wax API service
 */
object RetrofitClient {
    // List of all chains supported in this app, using IDs from CoinGecko API
    val KNOWN_CHAINS = listOf("ethereum", "wax").joinToString(",")

    private const val WAX_URL = "https://api.wax.alohaeos.com/"
    private const val COIN_GECKO_URL = "https://api.coingecko.com/api/v3/"

    val waxApi: WaxApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WAX_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WaxApiService::class.java)
    }

    val coinGeckoApi: CoinGeckoApi by lazy {
        Retrofit.Builder()
            .baseUrl(COIN_GECKO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinGeckoApi::class.java)
    }
}