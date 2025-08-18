package com.simplifynow.cryptowatcher.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.simplifynow.cryptowatcher.R

/**
 * Dialog that validates input for a wallet address
*/
@Composable
fun InputDialog(
    @StringRes titleRes: Int,
    tag: String,
    regex: Regex,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val context = LocalContext.current
    var textValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun isValid(text: String): Boolean = text.matches(regex)

    fun handleConfirm() {
        Log.d(tag, "onConfirm: $textValue")
        if (isValid(textValue)) {
            Log.d(tag, "Valid input")
            onConfirm(textValue)
            onDismiss()
        } else {
            Log.d(tag, "Invalid input")
            errorMessage = context.getString(R.string.invalid_address)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(titleRes)) },
        text = {
            Column {
                TextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                        if (errorMessage.isNotEmpty()) errorMessage = ""
                    },
                    placeholder = { Text(stringResource(R.string.type_here)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { handleConfirm() })
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
            Button(onClick = { handleConfirm() }) {
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