package com.simplifynow.cryptowatcher.ui

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.simplifynow.cryptowatcher.R

/**
 * Prompt user to confirm deleting a card
 * @param onDeleteConfirmed the function to call when the ok button is clicked
 * @param showDialog the visibility state of the dialog
 */
@Composable
fun DeleteCardConfirmation(onDeleteConfirmed: () -> Unit, showDialog: MutableState<Boolean>) {
    Log.d("DeleteConfirmation", "Clicked")

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(stringResource(R.string.confirm)) },
            text = { Text(stringResource(R.string.delete_prompt)) },
            confirmButton = {
                Button(onClick = {
                    onDeleteConfirmed()
                    Log.d("DeleteConfirmation", "Item deleted")
                    showDialog.value = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}