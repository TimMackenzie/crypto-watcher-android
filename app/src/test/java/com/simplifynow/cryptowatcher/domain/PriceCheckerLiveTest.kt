package com.simplifynow.cryptowatcher.domain

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Test

class PriceCheckerLiveTest {

    @Test
    fun live_simplePrice_returnsPositiveUsdForEthAndWax() = runBlocking {
        val checker = PriceChecker()

        // Small retry loop to handle transient failures / 429s
        val maxAttempts = 3
        var lastError: Throwable? = null
        var prices: Map<String, Double>? = null

        repeat(maxAttempts) { attempt ->
            try {
                prices = withTimeout(10_000) { checker.getPrices() }
                return@repeat
            } catch (t: Throwable) {
                lastError = t
                // basic backoff: 500ms, 1s
                delay(500L * (attempt + 1))
            }
        }

        val result = prices ?: throw AssertionError("Failed to fetch live prices", lastError)

        // Must contain both ids
        assertTrue("Result must contain 'ethereum'", result.containsKey("ethereum"))
        assertTrue("Result must contain 'wax'", result.containsKey("wax"))

        // Values should be finite and > 0
        val eth = result["ethereum"]
        val wax = result["wax"]

        assertNotNull("ETH price is null", eth)
        assertNotNull("WAX price is null", wax)

        assertTrue("ETH USD must be > 0, was $eth", eth!!.isFinite() && eth > 0.0)
        assertTrue("WAX USD must be > 0, was $wax", wax!!.isFinite() && wax > 0.0)
    }
}