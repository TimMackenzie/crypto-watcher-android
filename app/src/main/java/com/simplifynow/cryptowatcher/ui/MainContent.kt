package com.simplifynow.cryptowatcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Show either empty state view or the content.
 * @param padding the padding to apply to the content, needed to account for the app bar
 */
@OptIn(ExperimentalMaterial3Api::class) // for PTR
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    viewModel: CryptoWatcherViewModel = hiltViewModel()
) {
    val adapterList = viewModel.balanceCardData.collectAsState().value
    val refreshing by viewModel.isLoading.collectAsState()

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = { viewModel.load() },
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            if (adapterList.isNotEmpty()) {
                ColumnContentFromData(adapterList)
            } else {
                Text("Add wallets to start", modifier = Modifier.padding(16.dp))
            }
        }
    }
}