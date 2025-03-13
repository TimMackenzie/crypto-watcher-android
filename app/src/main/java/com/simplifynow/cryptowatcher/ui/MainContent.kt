package com.simplifynow.cryptowatcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Show either empty state view or the content.
 * @param padding the padding to apply to the content, needed to account for the app bar
 */
@Composable
fun MainContent(padding: PaddingValues) {
    val adapterList = getDataSourcesState().value

    Column (
        modifier = Modifier.padding(padding)
    ) {
        if (adapterList.isNotEmpty()) {
            ColumnContentFromData(adapterList)
        } else {
            Text("Add wallets to start", modifier = Modifier.padding(16.dp))
        }
    }
}