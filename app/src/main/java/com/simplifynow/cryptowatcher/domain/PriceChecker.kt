package com.simplifynow.cryptowatcher.domain

import com.simplifynow.cryptowatcher.data.network.CoinGeckoApi
import com.simplifynow.cryptowatcher.data.network.RetrofitClient


class PriceChecker(
    private val api: CoinGeckoApi = RetrofitClient.coinGeckoApi
) {
    suspend fun getPrices(): Map<String, Double> {
        val response = api.getSimplePrice(RetrofitClient.KNOWN_CHAINS, "usd")
        return response.mapValues { it.value.usd ?: Double.NaN }
    }
}