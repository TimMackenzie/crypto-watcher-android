package com.simplifynow.cryptowatcher.domain

import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Helper object for managing wallets
 */
object WalletHelper {
    /**
     * Add pre-defined test wallets to preferences
     */
    fun addTestWallets(walletPreferences: WalletPreferences) {
        val walletPairs = listOf(
            WalletPreferences.WalletPair(WalletPreferences.WalletType.WAX, "battleminers"),
            WalletPreferences.WalletPair(WalletPreferences.WalletType.WAX, "eosio"),
            WalletPreferences.WalletPair(WalletPreferences.WalletType.ETH, "0x0000000000000000000000000000000000000000"),

            )

        CoroutineScope(Dispatchers.IO).launch {
            walletPreferences.saveWalletPairs(walletPairs)
        }
    }

    /**
     * Remove all wallets from the preferences
     */
    fun clearWallets(walletPreferences: WalletPreferences) {
        CoroutineScope(Dispatchers.IO).launch {
            walletPreferences.saveWalletPairs(emptyList())
        }
    }
}