package com.ravimaurya.urjanext.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun AlertDialogUrja(
    title: String,
    text: String = "",
    confirmButtonText: String = "Confirm",
    dismissButtonText: String = "Cancel",
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
){

    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(
                onClick = { onConfirmClick() }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismissClick() }
            ) {
                Text(dismissButtonText)
            }
        },
        onDismissRequest = {
            onDismissClick()
        }
    )

}