package com.simplifynow.cryptowatcher.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
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
@OptIn(ExperimentalMaterialApi::class) // for PTR
@Composable
fun MainContent(
    padding: PaddingValues,
    viewModel: CryptoWatcherViewModel = hiltViewModel()
) {
    val adapterList = viewModel.balanceCardData.collectAsState().value
    val refreshing by viewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { viewModel.load() }
    )

    Box(
        modifier = Modifier
            .padding(padding)
            .pullRefresh(pullRefreshState)
    ) {
        Column {
            if (adapterList.isNotEmpty()) {
                ColumnContentFromData(adapterList)
            } else {
                Text("Add wallets to start", modifier = Modifier.padding(16.dp))
            }
        }
        PullRefreshIndicator(
            refreshing,
            pullRefreshState,
            Modifier.align(androidx.compose.ui.Alignment.TopCenter)
        )
    }
}