package com.simplifynow.cryptowatcher.ui

import androidx.compose.runtime.Composable
import com.simplifynow.cryptowatcher.R


/**
 * Allow user to enter a wax wallet address
 */
@Composable
fun WaxInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val waxRegex = "^[a-z][a-z1-5.]{1,11}$".toRegex() // 2–12 chars, starts with a-z, then a-z/1-5/.
    InputDialog(
        titleRes = R.string.add_a_wax_wallet,
        tag = "WaxInputDialog",
        regex = waxRegex,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}