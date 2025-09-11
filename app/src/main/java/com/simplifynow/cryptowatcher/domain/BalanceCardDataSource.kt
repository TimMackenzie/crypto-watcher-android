package com.simplifynow.cryptowatcher.domain

import androidx.annotation.DrawableRes
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines the data source for a balance card.  It includes
 * - A loading item
 * - The main flow with data
 * - The wallet pair needed if removing this data source
 */
interface BalanceCardDataSource {
    fun getLoadingItem(): BalanceItem
    fun getBalanceCard(): Flow<BalanceItem>
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