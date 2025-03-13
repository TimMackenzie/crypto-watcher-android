package com.simplifynow.cryptowatcher.domain

import android.content.Context
import android.util.Log
import com.simplifynow.cryptowatcher.R
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.data.network.EthBalanceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Provide a flow that emits the balance of the given Ethereum address.
 */
class EthChecker(
    private val context: Context,
    private val address: String
) : BalanceCardDataSource {
    override fun getWalletPair() = WalletPreferences.WalletPair(WalletPreferences.WalletType.ETH, address)

    override fun getLoadingItem(): BalanceCardDataSource.BalanceItem {
        return BalanceCardDataSource.BalanceItem(
            iconId = R.drawable.ic_downloading,
            chainName = context.getString(R.string.chain_eth),
            address = address,
            balance = context.getString(R.string.loading)
        )
    }

    override fun getBalanceCard(): Flow<BalanceCardDataSource.BalanceItem> = flow {
        try {
            val balance = EthBalanceRequest.getBalance(address)

            emit(
                BalanceCardDataSource.BalanceItem(
                    iconId = R.drawable.ic_crypto_ethereum,
                    chainName = context.getString(R.string.chain_eth),
                    address = address,
                    balance = balance?.toPlainString() ?: context.getString(R.string.data_error)
                )
            )
        } catch (e: Exception) {
            Log.e("EthChecker", "Error getting eth balance", e)

            emit(
                BalanceCardDataSource.BalanceItem(
                    iconId = R.drawable.ic_crypto_ethereum,
                    chainName = context.getString(R.string.chain_eth),
                    address = address,
                    balance = context.getString(R.string.network_error)
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}