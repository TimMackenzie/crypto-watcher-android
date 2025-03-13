package com.simplifynow.cryptowatcher.data.network

/**
 * Data class for the balance request, requesting only the wax balance
 */
data class WaxBalanceRequest(
    val code: String = "eosio.token",
    val account: String,
    val symbol: String = "WAX"
)