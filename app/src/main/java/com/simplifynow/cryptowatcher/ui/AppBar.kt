package com.simplifynow.cryptowatcher.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.simplifynow.cryptowatcher.R

/**
 * Show the app bar with the title
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}