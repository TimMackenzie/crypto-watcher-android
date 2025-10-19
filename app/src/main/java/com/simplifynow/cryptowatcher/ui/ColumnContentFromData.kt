package com.simplifynow.cryptowatcher.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplifynow.cryptowatcher.domain.BalanceCardDataSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

/**
 * Supply a lazy column with cards loaded from individual data sources
 * Provides onClick that deletes this card
 * @param adapterList The list of data sources to load
 */
@Composable
fun ColumnContentFromData(
    adapterList: ImmutableList<BalanceCardDataSource>,
    modifier: Modifier = Modifier,
    viewModel: CryptoWatcherViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val prices by viewModel.usdPrices.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Log.d("ColumnContentFromData", "Loading: ${adapterList.size}")
        items(adapterList) { item ->
            Log.d("ColumnContentFromData", "Loading: ${item.getWalletPair().type}: ${item.getWalletPair().address}")
            val cardData by item.getBalanceCard().collectAsState()
            BalanceCard(
                item = cardData,
                prices = prices,
                onClick = {
                    Log.d("MainActivity", "Clicked to delete")
                    coroutineScope.launch {
                        viewModel.getPreferences().removeWalletPair(
                            item.getWalletPair()
                        )
                    }
                }
            )
        }
    }
}