package com.simplifynow.cryptowatcher.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplifynow.cryptowatcher.domain.BalanceCardDataSource
import com.simplifynow.cryptowatcher.domain.WalletPairToDataSource.getBalanceCardDataSources

/**
 * Read the stored preferences for wallets, and process into a list of data sources.
 * Use State to avoid recomposition for use with composable
 */
@Composable
fun getDataSourcesState(viewModel: WalletViewModel = hiltViewModel()): State<List<BalanceCardDataSource>> {
    val dataSourcesState = remember { mutableStateOf(emptyList<BalanceCardDataSource>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        getBalanceCardDataSources(context, viewModel.getPreferences())
            .collect { dataSourcesState.value = it }
    }

    return dataSourcesState
}