package com.simplifynow.cryptowatcher.domain

import android.content.Context
import android.util.Log
import com.simplifynow.cryptowatcher.R
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.data.network.RetrofitClient
import com.simplifynow.cryptowatcher.data.network.WaxBalanceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Provide a flow that emits the balance of the given wax address
 */
class WaxChecker(
    private val context: Context,
    private val address: String
) : BalanceCardDataSource {
    override fun getWalletPair() = WalletPreferences.WalletPair(WalletPreferences.WalletType.WAX, address)


    override fun getLoadingItem(): BalanceCardDataSource.BalanceItem {
        return BalanceCardDataSource.BalanceItem(
            iconId = R.drawable.ic_downloading,
            chainName = context.getString(R.string.chain_name_wax),
            chainId = context.getString(R.string.chain_id_wax),
            address = address,
            balance = context.getString(R.string.loading)
        )
    }

    override fun getBalanceCard(): Flow<BalanceCardDataSource.BalanceItem> = flow {
        Log.d("WaxChecker", "Getting wax balance for $address")
        try {
            val response = RetrofitClient.waxApi.getCurrencyBalance(
                WaxBalanceRequest(account = address)
            ).execute()

            Log.d("WaxChecker", "completed request")


            emit(
                BalanceCardDataSource.BalanceItem(
                    iconId = R.drawable.ic_crypto_wax,
                    chainName = context.getString(R.string.chain_name_wax),
                    chainId = context.getString(R.string.chain_id_wax),
                    address = address,
                    balance = response.body()?.getOrNull(0) ?: context.getString(R.string.data_error)
                )
            )
        } catch (e: Exception) {
            Log.e("WaxChecker", "Error getting wax balance", e)

            emit(
                BalanceCardDataSource.BalanceItem(
                    iconId = R.drawable.ic_crypto_wax,
                    chainName = context.getString(R.string.chain_name_wax),
                    chainId = context.getString(R.string.chain_id_wax),
                    address = address,
                    balance = context.getString(R.string.network_error)
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}