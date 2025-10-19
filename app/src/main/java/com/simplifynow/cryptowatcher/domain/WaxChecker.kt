package com.simplifynow.cryptowatcher.domain

import android.content.Context
import android.util.Log
import com.simplifynow.cryptowatcher.R
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.data.network.RetrofitClient
import com.simplifynow.cryptowatcher.data.network.WaxBalanceRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * Provide a flow that emits the balance of the given wax address
 */
class WaxChecker(
    private val context: Context,
    private val address: String
) : BalanceCardDataSource {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val loadingItem = BalanceCardDataSource.BalanceItem(
        iconId = R.drawable.ic_downloading,
        chainName = context.getString(R.string.chain_name_wax),
        chainId = context.getString(R.string.chain_id_wax),
        address = address,
        balance = context.getString(R.string.loading)
    )

    private val errorItem = BalanceCardDataSource.BalanceItem(
        iconId = R.drawable.ic_crypto_wax,
        chainName = context.getString(R.string.chain_name_wax),
        chainId = context.getString(R.string.chain_id_wax),
        address = address,
        balance = context.getString(R.string.network_error)
    )

    private val balanceState: StateFlow<BalanceCardDataSource.BalanceItem> by lazy {
        flow {
            Log.d("WaxChecker", "Getting wax balance for $address")
            val response = RetrofitClient.waxApi.getCurrencyBalance(
                WaxBalanceRequest(account = address)
            ).execute()
            Log.d("WaxChecker", "completed request for $address")

            emit(
                BalanceCardDataSource.BalanceItem(
                    iconId = R.drawable.ic_crypto_wax,
                    chainName = context.getString(R.string.chain_name_wax),
                    chainId = context.getString(R.string.chain_id_wax),
                    address = address,
                    balance = response.body()?.getOrNull(0)
                        ?: context.getString(R.string.data_error)
                )
            )
        }
            .catch { e ->
                if (e is CancellationException) {
                    Log.d("WaxChecker", "Request cancelled")
                } else {
                    Log.e("WaxChecker", "Error getting wax balance", e)
                    emit(errorItem)
                }
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = loadingItem
            )
    }

    override fun getWalletPair() = WalletPreferences.WalletPair(WalletPreferences.WalletType.WAX, address)
    override fun getBalanceCard(): StateFlow<BalanceCardDataSource.BalanceItem> = balanceState
}