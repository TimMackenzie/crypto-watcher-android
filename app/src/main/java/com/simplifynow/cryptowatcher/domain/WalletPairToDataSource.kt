package com.simplifynow.cryptowatcher.domain

import android.content.Context
import android.util.Log
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.data.local.WalletPreferences.WalletPair
import com.simplifynow.cryptowatcher.data.local.WalletPreferences.WalletType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object WalletPairToDataSource {
    /**
     * Translate from wallet pair from DataStore into data source
     */
    private fun transformPairToDataSource(context: Context, pair: WalletPair): BalanceCardDataSource {
        Log.d("WalletPreferences", "transformPairToDataSource: $pair")
        return when (pair.type) {
            WalletType.WAX -> WaxChecker(context, pair.address)
            WalletType.ETH -> EthChecker(context, pair.address)
        }
    }

    /**
     * Get all wallet pairs from DataStore, and process into a list of data sources
     * @param context should be an applicationContext
     */
    fun getBalanceCardDataSources(context: Context, walletPreferences: WalletPreferences): Flow<List<BalanceCardDataSource>> {
        return walletPreferences.getWalletPairs().map { list ->
            list.map { transformPairToDataSource(context, it) }
        }
    }
}