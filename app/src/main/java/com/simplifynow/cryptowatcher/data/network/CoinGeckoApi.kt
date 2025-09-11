package com.simplifynow.cryptowatcher.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("simple/price")
    suspend fun getSimplePrice(
        @Query("ids") ids: String,              // e.g. "ethereum,wax"
        @Query("vs_currencies") vs: String = "usd"
    ): Map<String, PriceResponse>
}

data class PriceResponse(
    val usd: Double? = null
)