package com.ravimaurya.urjanext.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CPPreview(modifier: Modifier = Modifier) {
    CircularProgressDialog(true)
}

@Composable
fun CircularProgressDialog(
    isLoading: Boolean
) {
    if (isLoading){
        Dialog(
            onDismissRequest = {

            }
        ) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun CircularProgressBar(
    isLoading: Boolean,
) {
    if (isLoading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(color = Color.Blue)
        }
    }

}