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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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
    private val _balanceCardData = MutableStateFlow(persistentListOf<BalanceCardDataSource>())
    val balanceCardData: StateFlow<ImmutableList<BalanceCardDataSource>> = _balanceCardData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _usdPrices = MutableStateFlow<ImmutableMap<String, Double>>(persistentMapOf())
    val usdPrices: StateFlow<ImmutableMap<String, Double>> = _usdPrices

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
        Log.d("CryptoWatcherViewModel", "Loading data")
        loadJob?.cancel() // cancel any previous collection
        loadJob = viewModelScope.launch {
            _isLoading.value = true
            var firstEmission = true
            try {
                getBalanceCardDataSources(
                    context = appContext,
                    walletPreferences = getPreferences()
                )
                    .distinctUntilChanged()
                    .collect { list ->
                        _balanceCardData.value = list.toPersistentList()
                        if (firstEmission) {
                            Log.d("CryptoWatcherViewModel", "Loaded data sources - initial")
                            _isLoading.value = false
                            firstEmission = false
                        } else {
                            Log.d("CryptoWatcherViewModel", "Loaded data sources from preferences change")
                        }
                    }
            } catch (e: CancellationException) {
                Log.d("CryptoWatcherViewModel", "Load job cancelled", e)
                _balanceCardData.value = persistentListOf()
            } catch (e: Exception) {
                Log.e("CryptoWatcherViewModel", "Exception loading data sources", e)
                _balanceCardData.value = persistentListOf()
            } finally {
                _isLoading.value = false
            }
        }

        priceJob?.cancel() // cancel any previous collection
        priceJob = viewModelScope.launch {
            Log.d("CryptoWatcherViewModel", "Loading USD prices")
            try {
                _usdPrices.value = PriceChecker().getPrices().toPersistentMap()
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