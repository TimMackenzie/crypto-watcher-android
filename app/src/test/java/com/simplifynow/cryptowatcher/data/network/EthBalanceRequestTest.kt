package com.simplifynow.cryptowatcher.data.network

import com.google.common.truth.Truth.assertThat
import com.simplifynow.cryptowatcher.BuildConfig
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.exceptions.ClientConnectionException
import org.web3j.protocol.http.HttpService
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

/**
 * Validate EthBalanceRequest against the Ethereum mainnet
 * Some of the tests here utilize Infura directly, so be wary of excessive use
 * The free account has a low limit of requests per second, so tests here are throttled
 */
class EthBalanceRequestTest {
    companion object {
        const val ORIGIN_ADDRESS = "0x0000000000000000000000000000000000000000"
        const val ADDRESS_1 = "0x742d35Cc6634C0532925a3b844Bc454e4438f44e"

        var currentDelay = 0L
    }

    /**
     * To avoid rate throttling, ensure each live data test queues up shortly after the last one
     */
    private fun liveDataDelay() {
        Thread.sleep(currentDelay)
        currentDelay += 100
    }

    /**
     * Ensure that the build config is properly configured and can load the endpoint URL
     */
    @Test
    fun test_buildconfig() {
        assertThat(BuildConfig.evmEndpoint).isNotEmpty()
    }

    /**
     * Validate with live data to ensure that the service can use the current API
     */
    @Test
    fun test_getBalance_livedata() {
        liveDataDelay()

        val balance = EthBalanceRequest().getBalance(ORIGIN_ADDRESS)

        // Actual balance is always growing, so test against a minimum value.  We don't need precision here.
        val truncatedBalance = balance?.setScale(0, RoundingMode.FLOOR)
        val intBalance = truncatedBalance?.toInt()
        assertThat(intBalance).isNotNull()
        assertThat(intBalance!! > 13000).isTrue()
    }

    @Test
    fun `getBalance with a valid Ethereum address and positive balance`() {
        val web3j = setupWeb3jMock(
            address = ADDRESS_1,
            balanceWei = BigInteger("1000000000000000000"))

        val expectedEther = BigDecimal("1.0")

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val actualBalance = ethBalanceRequest.getBalance(ADDRESS_1)

        assertThat(expectedEther.compareTo(actualBalance)).isEqualTo(0)
    }

    @Test
    fun `getBalance with a valid Ethereum address and zero balance`() {
        val address = "0x742d35Cc6634C0532925a3b844Bc454e4438f44e"

        val web3j = setupWeb3jMock(
            address = address,
            balanceWei = BigInteger("0"))

        val expectedEther = BigDecimal("0.0")

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val actualBalance = ethBalanceRequest.getBalance(address)

        assertThat(expectedEther.compareTo(actualBalance)).isEqualTo(0)
    }

    @Test
    fun `getBalance with an invalid Ethereum address format`() {
        liveDataDelay()

        val balance = EthBalanceRequest().getBalance("0xabcd")
        assertThat(balance).isNull()
    }

    @Test
    fun `getBalance with an empty string as address`() {
        liveDataDelay()

        val balance = EthBalanceRequest().getBalance("")
        assertThat(balance).isNull()
    }

    @Test
    fun `getBalance when API endpoint is unavailable`() {
        val httpService = mockk<HttpService>()
        every { httpService.send<EthGetBalance>(any(), any()) } throws
                ClientConnectionException("503 Service Unavailable")
        val web3j = Web3j.build(httpService)

        val ethBalanceRequest = EthBalanceRequest(web3j)
        val actualBalance = ethBalanceRequest.getBalance(ORIGIN_ADDRESS)
        assertThat(actualBalance).isNull()
    }

    @Test
    fun `getBalance with network timeout`() {
        val httpService = mockk<HttpService>()
        every { httpService.send<EthGetBalance>(any(), any()) } throws IOException("timeout")
        val web3j = Web3j.build(httpService)

        val ethBalanceRequest = EthBalanceRequest(web3j)
        val actualBalance = ethBalanceRequest.getBalance(ORIGIN_ADDRESS)
        assertThat(actualBalance).isNull()
    }

    @Test
    fun `getBalance with very large balance in Wei `() {
        val web3j = setupWeb3jMock(
            address = ADDRESS_1,
            balanceWei = BigInteger("100000000000000000000000001"))

        val expectedEther = BigDecimal("100000000.000000000000000001")

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val actualBalance = ethBalanceRequest.getBalance(ADDRESS_1)

        assertThat(expectedEther.compareTo(actualBalance)).isEqualTo(0)
    }


    @Test
    fun `getBalance when EthGetBalance returns null`() {
        val web3j = setupWeb3jMock(
            address = ORIGIN_ADDRESS,
            balanceWei = null)

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val actualBalance = ethBalanceRequest.getBalance(ORIGIN_ADDRESS)

        assertThat(actualBalance).isNull()
    }

    @Test
    fun `getBalance for an address with a very small  non zero balance  dust `() {
        val web3j = setupWeb3jMock(
            address = ADDRESS_1,
            balanceWei = BigInteger("1"))

        val expectedEther = BigDecimal("0.000000000000000001")

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val actualBalance = ethBalanceRequest.getBalance(ADDRESS_1)

        assertThat(expectedEther.compareTo(actualBalance)).isEqualTo(0)
    }

    @Test
    fun `Concurrency test for getBalance`() = runBlocking {
        val web3j = setupWeb3jMock(
            address = ORIGIN_ADDRESS,
            balanceWei = BigInteger("1000000000000000000"))

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val results = (1..20).map {
            async {
                ethBalanceRequest.getBalance(ORIGIN_ADDRESS)
            }
        }.awaitAll()

        // Ensure all requests return a valid result
        results.forEach { result ->
            assertThat(result).isNotNull()
        }
    }

    /**
     * This test verifies that when a 429 error is encountered due to hitting the rate limit,
     *  the response is null.
     */
    @Test
    fun `Concurrency test for getBalance - over limit`() = runBlocking {
        val httpService = mockk<HttpService>()
        every { httpService.send<EthGetBalance>(any(), any()) } throws
                ClientConnectionException("429 Too Many Requests")
        val web3j = Web3j.build(httpService)

        val ethBalanceRequest = EthBalanceRequest(web3j)

        val results = (1..20).map {
            async {
                ethBalanceRequest.getBalance(ORIGIN_ADDRESS)
            }
        }.awaitAll()

        // At least one of these queries will have a null result (due to mocking, all will)
        assertThat(results.any { it == null }).isTrue()
    }

    /**
     * Create a web3j mock for specified address that will return the specified balance
     */
    private fun setupWeb3jMock(
        address: String,
        balanceWei: BigInteger?) : Web3j {
        val web3j = mockk<Web3j>()
        val ethGetBalanceResponse = mockk<EthGetBalance>()
        val request = mockk<org.web3j.protocol.core.Request<*, EthGetBalance>>()

        every { web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST) } returns request
        every { request.send() } returns ethGetBalanceResponse
        every { ethGetBalanceResponse.balance } returns balanceWei

        return web3j
    }
}