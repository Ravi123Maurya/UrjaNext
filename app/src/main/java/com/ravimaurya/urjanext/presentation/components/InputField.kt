package com.ravimaurya.urjanext.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ravimaurya.urjanext.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OutlinedInputFieldPreview(){
    OutlinedInputField(value = "Text", {})
}

@Composable
fun OutlinedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: Int? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
    readOnly: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = stringResource(label!!)) },
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = ""
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = ""
                )
            }
        },
        shape = ShapeDefaults.ExtraLarge,
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            unfocusedLeadingIconColor = Color.Gray
        ),
        readOnly = readOnly,
        enabled = enabled
    )
}