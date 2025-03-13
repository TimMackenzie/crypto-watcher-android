package com.simplifynow.cryptowatcher

import com.google.common.truth.Truth.assertThat
import com.simplifynow.cryptowatcher.data.network.RetrofitClient
import com.simplifynow.cryptowatcher.data.network.WaxBalanceRequest
import org.junit.Test

class WaxApiServiceTest {
    /**
     * Validate with live data to ensure that the service can use the current API
     */
    @Test
    fun test_getCurrencyBalance() {
        val response = RetrofitClient.instance.getCurrencyBalance(
            WaxBalanceRequest(
                "eosio.token",
                "eosio",
                "WAX"
            )
        ).execute()

        assertThat(response).isNotNull()

        val balanceList = response.body()
        assertThat(balanceList).isNotNull()
        assertThat(balanceList?.size).isEqualTo(1)

        val balance = balanceList?.getOrNull(0) ?: "0 WAX"
        assertThat(balance).isEqualTo("5232.59675090 WAX") // this is TBR, as the value may change
    }
}