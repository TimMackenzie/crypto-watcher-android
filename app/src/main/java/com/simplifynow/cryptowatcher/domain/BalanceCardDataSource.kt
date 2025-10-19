package com.simplifynow.cryptowatcher.domain

import androidx.annotation.DrawableRes
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import kotlinx.coroutines.flow.StateFlow

/**
 * This interface defines the data source for a balance card.  It includes
 * - The main flow with data and initial state
 * - The wallet pair needed if removing this data source
 */
interface BalanceCardDataSource {
    fun getBalanceCard(): StateFlow<BalanceItem>
    fun getWalletPair(): WalletPreferences.WalletPair

    /**
     * A BalanceItem contains the data needed to display a balance card.
     */
    data class BalanceItem(
        @DrawableRes val iconId: Int,
        val chainName: String,
        val chainId: String,
        val address: String,
        val balance: String
    )
}