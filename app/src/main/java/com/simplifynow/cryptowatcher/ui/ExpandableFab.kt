package com.simplifynow.cryptowatcher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simplifynow.cryptowatcher.R
import com.simplifynow.cryptowatcher.data.local.WalletPreferences
import com.simplifynow.cryptowatcher.domain.WalletHelper.addTestWallets
import com.simplifynow.cryptowatcher.domain.WalletHelper.clearWallets
import kotlinx.coroutines.launch

/**
 * Expandable FAB with options for each wallet type, deleting all wallets, and adding test wallets
 */
@Composable
fun ExpandableFab(
    modifier: Modifier = Modifier,
    viewModel: CryptoWatcherViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var showWaxInput by remember { mutableStateOf(false) }
    var showEvmInput by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (expanded) {
                FabButton(
                    icon = ImageVector.vectorResource(R.drawable.ic_crypto_wax),
                    label = "Wax",
                ) {
                    println("Wax Clicked")
                    showWaxInput = true
                    expanded = false
                }
                FabButton(
                    icon = ImageVector.vectorResource(R.drawable.ic_crypto_ethereum),
                    label = "EVM"
                ) {
                    println("EVM Clicked")
                    showEvmInput = true
                    expanded = false
                }
                FabButton(icon = Icons.Default.Favorite, label = stringResource(R.string.add_all_test_accounts)) {
                    println("Add all clicked")
                    coroutineScope.launch {
                        addTestWallets(viewModel.getPreferences())
                    }
                    expanded = false
                }
                FabButton(icon = Icons.Default.Delete, label = stringResource(R.string.clear_all)) {
                    println("Clear all clicked")
                    coroutineScope.launch {
                        clearWallets(viewModel.getPreferences())
                    }
                    expanded = false
                }
            }

            if (showWaxInput) {
                WaxInputDialog(
                    onDismiss = { showWaxInput = false },
                    onConfirm = { address ->
                        coroutineScope.launch {
                            viewModel.getPreferences().addWalletPair(
                                WalletPreferences.WalletPair(
                                    WalletPreferences.WalletType.WAX,
                                    address
                                )
                            )
                        }
                    }
                )
            }

            if (showEvmInput) {
                EvmInputDialog(
                    onDismiss = { showEvmInput = false },
                    onConfirm = { address ->
                        coroutineScope.launch {
                            viewModel.getPreferences().addWalletPair(
                                WalletPreferences.WalletPair(
                                    WalletPreferences.WalletType.ETH,
                                    address
                                )
                            )
                        }
                    }
                )
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = stringResource(R.string.toggle_fab)
                )
            }
        }
    }
}