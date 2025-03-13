package com.simplifynow.cryptowatcher

import com.google.common.truth.Truth.assertThat
import com.simplifynow.cryptowatcher.data.network.EthBalanceRequest
import org.junit.Test
import java.math.RoundingMode

class EthBalanceRequestTest {
    /**
     * Ensure that the build config is properly configured and can load the Infura project ID
     */
    @Test
    fun test_buildconfig() {
        assertThat(BuildConfig.infuraProjectId).isNotEmpty()
    }

    /**
     * Validate with live data to ensure that the service can use the current API
     */
    @Test
    fun test_getBalance() {
        val balance = EthBalanceRequest.getBalance("0x0000000000000000000000000000000000000000")

        // Actual balance is always growing, so test against a minimum value
        val truncatedBalance = balance?.setScale(0, RoundingMode.FLOOR)
        val intBalance = truncatedBalance?.toInt()
        assertThat(intBalance).isNotNull()
        assertThat(intBalance!! > 13000).isTrue()
    }
}