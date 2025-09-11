package com.simplifynow.cryptowatcher.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplifynow.cryptowatcher.domain.BalanceCardDataSource
import kotlinx.coroutines.launch

/**
 * Supply a lazy column with cards loaded from individual data sources
 * Provides onClick that deletes this card
 * @param adapterList The list of data sources to load
 */
@Composable
fun ColumnContentFromData(adapterList: List<BalanceCardDataSource>, viewModel: CryptoWatcherViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(adapterList) { item ->
            BalanceCard(
                item = item.getBalanceCard().collectAsState(
                    item.getLoadingItem()
                ).value,
                prices = viewModel.usdPrices.collectAsState().value,

            ) {
                Log.d("MainActivity", "Clicked to delete")
                coroutineScope.launch {
                    viewModel.getPreferences().removeWalletPair(
                        item.getWalletPair()
                    )
                }
            }
        }
    }
}