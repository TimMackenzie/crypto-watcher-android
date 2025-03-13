package com.simplifynow.cryptowatcher.ui

import androidx.lifecycle.ViewModel
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This ViewModel exists to support injecting the WalletPreferences implementation
 * It is injected into composable functions via the parameter
 *  viewModel: WalletViewModel = hiltViewModel()
 * As the app expands, this would play a more central role with more data
 */
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletPreferences: WalletPreferences
) : ViewModel() {
    /**
     * The view model needs to be a separate entity to allow DI for the preferences, but in this
     * case there is no value added in adding another layer of abstraction by having a separate
     * functions to access each function in the preferences object.
     */
    fun getPreferences() : WalletPreferences = walletPreferences
}