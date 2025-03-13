package com.simplifynow.cryptowatcher.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton to instantiate the wax API service
 */
object RetrofitClient {
    private const val BASE_URL = "https://api.wax.alohaeos.com/"

    val instance: WaxApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WaxApiService::class.java)
    }
}