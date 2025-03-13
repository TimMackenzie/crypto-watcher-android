package com.simplifynow.cryptowatcher.data.network

import com.simplifynow.cryptowatcher.BuildConfig
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.BigDecimal

object EthBalanceRequest {
    // Load the private key from the local.properties file via the BuildConfig
    private val web3 = Web3j.build(HttpService("https://mainnet.infura.io/v3/${BuildConfig.infuraProjectId}"))

    /**
     * Retrieves the Ethereum balance for a given address.
     */
    fun getBalance(address: String): BigDecimal? {
        try {
            // Get the balance in Wei (the smallest unit of Ether)
            val balanceInWei = web3.ethGetBalance(
                address,
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            ).send().balance

            // Convert the balance from Wei to Ether (in a human-readable format)
            val balanceInEther = Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER)
            println("Ethereum Balance for $address: $balanceInEther ETH")

            return balanceInEther
        } catch (e: Exception) {
            println("Error retrieving balance: ${e.message}")
        }

        return null
    }
}