package com.simplifynow.cryptowatcher.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.simplifynow.cryptowatcher.R

/**
 * Allow user to input a wax address
 */
@Composable
fun WaxInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    /**
     * Perform basic validation that this seems like a valid wax address, from 2 to 12 chars
     */
    fun isValidWaxAddress(address: String): Boolean {
        val waxRegex = "^[a-z][a-z1-5.]{1,11}$".toRegex()
        return address.matches(waxRegex)
    }

    var textValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_a_wax_wallet)) },
        text = {
            Column {
                TextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = { Text(stringResource(R.string.type_here)) }
                )
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                Log.d("WaxInputDialog", "onConfirm: $textValue")
                if (isValidWaxAddress(textValue)) {
                    Log.d("WaxInputDialog", "Valid address")
                    onConfirm(textValue)
                    onDismiss()
                } else {
                    Log.d("WaxInputDialog", "Invalid address")
                    errorMessage = context.getString(R.string.invalid_address)
                }
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}