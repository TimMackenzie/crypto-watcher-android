package com.simplifynow.cryptowatcher.data.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Service to request the balance of a wax account
 */
interface WaxApiService {
    /**
     * Request the wax balance of a wax account
     */
    @Headers("Content-Type: application/json")
    @POST("v1/chain/get_currency_balance")
    fun getCurrencyBalance(@Body request: WaxBalanceRequest): Call<List<String>>

}