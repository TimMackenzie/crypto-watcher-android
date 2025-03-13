package com.simplifynow.cryptowatcher.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

/**
 * Preference handler to save and retrieve wallet pairs
 */
interface WalletPreferences {
    /**
     * All currently supported wallet types (chains)
     */
    enum class WalletType {
        WAX,
        ETH
    }

    @Serializable
    data class WalletPair(val type: WalletType, val address: String)

    /**
     * Save the list of wallet pairs to the preferences, overwriting existing value
     */
    suspend fun saveWalletPairs(walletPairs: List<WalletPair>)

    /**
     * Retrieve all wallet pairs from the preferences in a flow
     */
    fun getWalletPairs(): Flow<List<WalletPair>>

    /**
     * Add a new wallet pair to the saved list of wallet pairs
     */
    fun addWalletPair(pair: WalletPair)

    /**
     * Remove a wallet pair from the saved list of wallet pairs
     */
    fun removeWalletPair(pair: WalletPair)
}