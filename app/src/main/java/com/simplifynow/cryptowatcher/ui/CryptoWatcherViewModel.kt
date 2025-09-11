package com.simplifynow.cryptowatcher.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.domain.BalanceCardDataSource
import com.simplifynow.cryptowatcher.domain.PriceChecker
import com.simplifynow.cryptowatcher.domain.WalletPairToDataSource.getBalanceCardDataSources
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This ViewModel is injected into composable functions via the parameter
 *  viewModel: CryptoWatcherViewModel = hiltViewModel()
 */
@HiltViewModel
class CryptoWatcherViewModel @Inject constructor(
    private val walletPreferences: WalletPreferences,
    @param:ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _balanceCardData = MutableStateFlow(emptyList<BalanceCardDataSource>())
    val balanceCardData: StateFlow<List<BalanceCardDataSource>> = _balanceCardData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _usdPrices = MutableStateFlow<Map<String, Double>>(emptyMap())
    val usdPrices: StateFlow<Map<String, Double>> = _usdPrices

    // Keep track of jobs to allow manual restart
    private var loadJob: Job? = null
    private var priceJob: Job? = null

    /**
     * Start updating the data source state flow based on the wallet preferences flow.  Calling
     *  this again cancels the previous operation and starts it again, allowing manual refresh of
     *  data when wallets haven't changed.  The isLoading flow is only true from the time the job is
     *  started until the first value is collected.  The price data job does not affect the loading
     *  state.
     */
    fun load() {
        loadJob?.cancel() // cancel any previous collection
        loadJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                getBalanceCardDataSources(
                    context = appContext,
                    walletPreferences = getPreferences()
                ).collect {
                    _balanceCardData.value = it
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("CryptoWatcherViewModel", "Exception loading data sources", e)
                _balanceCardData.value = emptyList()
                _isLoading.value = false
            }
        }

        priceJob?.cancel() // cancel any previous collection
        priceJob = viewModelScope.launch {
            try {
                _usdPrices.value = PriceChecker().getPrices()
            } catch (e: Exception) {
                Log.e("CryptoWatcherViewModel", "Exception fetching USD prices", e)
            }
        }
    }

    fun getPreferences() : WalletPreferences = walletPreferences

    /**
     * Start loading data immediately
     */
    init {
        load()
    }
}