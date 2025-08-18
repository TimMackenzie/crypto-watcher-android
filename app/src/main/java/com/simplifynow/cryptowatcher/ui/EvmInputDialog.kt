package com.simplifynow.cryptowatcher.ui

import androidx.compose.runtime.Composable
import com.simplifynow.cryptowatcher.R

/**
 * Allow user to input an ETH address
 */
@Composable
fun EvmInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val evmRegex = "^0x[a-fA-F0-9]{40}$".toRegex()
    InputDialog(
        titleRes = R.string.add_an_evm_wallet,
        tag = "EvmInputDialog",
        regex = evmRegex,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}