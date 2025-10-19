package com.simplifynow.cryptowatcher.domain

import android.content.Context
import android.util.Log
import com.simplifynow.cryptowatcher.R
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.data.network.EthBalanceRequest
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
 * Provide a flow that emits the balance of the given Ethereum address.
 */
class EthChecker(
    private val context: Context,
    private val address: String
) : BalanceCardDataSource {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val loadingItem = BalanceCardDataSource.BalanceItem(
        iconId = R.drawable.ic_downloading,
        chainName = context.getString(R.string.chain_name_eth),
        chainId = context.getString(R.string.chain_id_eth),
        address = address,
        balance = context.getString(R.string.loading)
    )

    private val errorItem = BalanceCardDataSource.BalanceItem(
        iconId = R.drawable.ic_crypto_ethereum,
        chainName = context.getString(R.string.chain_name_eth),
        chainId = context.getString(R.string.chain_id_eth),
        address = address,
        balance = context.getString(R.string.network_error)
    )

    private val balanceState: StateFlow<BalanceCardDataSource.BalanceItem> by lazy {
        flow {
            Log.d("EthChecker", "Getting eth balance for $address")
            val balance = EthBalanceRequest().getBalance(address)
            Log.d("EthChecker", "completed request for $address")
            emit(
                BalanceCardDataSource.BalanceItem(
                    iconId = R.drawable.ic_crypto_ethereum,
                    chainName = context.getString(R.string.chain_name_eth),
                    chainId = context.getString(R.string.chain_id_eth),
                    address = address,
                    balance = balance?.toPlainString() ?: context.getString(R.string.data_error)
                )
            )
        }
            .catch { e ->
                if (e is CancellationException) {
                    Log.d("EthChecker", "Request cancelled")
                } else {
                    Log.e("EthChecker", "Error getting eth balance", e)
                    emit(errorItem)
                }
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = loadingItem
            )
    }

    override fun getBalanceCard(): StateFlow<BalanceCardDataSource.BalanceItem> = balanceState
    override fun getWalletPair() = WalletPreferences.WalletPair(WalletPreferences.WalletType.ETH, address)
}