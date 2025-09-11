package com.simplifynow.cryptowatcher.data.network

import com.google.common.truth.Truth
import org.junit.Test

class WaxApiServiceTest {
    // Response pattern expected for Wax balance, in form "xxx.yyy WAX"
    private val waxPattern = Regex("^\\d+\\.\\d{2,} WAX$")

    /**
     * Validate with live data to ensure that the service can use the current API
     */
    @Test
    fun test_getCurrencyBalance() {
        val response = RetrofitClient.waxApi.getCurrencyBalance(
            WaxBalanceRequest(
                "eosio.token",
                "eosio",
                "WAX"
            )
        ).execute()

        Truth.assertThat(response).isNotNull()

        val balanceList = response.body()
        Truth.assertThat(balanceList).isNotNull()
        Truth.assertThat(balanceList?.size).isEqualTo(1)

        val balance = balanceList?.getOrNull(0) ?: "0 WAX"
        Truth.assertThat(waxPattern.matches(balance)).isTrue()
    }
}