package com.ravimaurya.urjanext.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneEnabled
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.BigButton

@Composable
fun LoginContent( onLoginClick: (String, String) -> Unit ){

    val context = LocalContext.current

    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {




            // Email Field
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Enter email") },
                value = email,
                placeholder = { Text("example@gmail.com") },
                onValueChange = {
                    email = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = ""
                    )
                },
                shape = ShapeDefaults.ExtraLarge,
                textStyle = MaterialTheme.typography.bodyMedium
            )

//            // Phone Number Field
//            Text(
//                text = stringResource(R.string.phone_number),
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Start
//            )
//            OutlinedTextField(
//                modifier = Modifier.fillMaxWidth(),
//                label = { Text(text = stringResource(R.string.enter_phone)) },
//                value = phoneNumber,
//                onValueChange = {
//                    phoneNumber = it
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Filled.PhoneEnabled,
//                        contentDescription = ""
//                    )
//                },
//                shape = ShapeDefaults.ExtraLarge,
//                textStyle = MaterialTheme.typography.bodyMedium
//            )

            // Password Field
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.enter_password)) },
                value = password,
                onValueChange = {
                    password = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = ""
                    )
                },
                shape = ShapeDefaults.ExtraLarge,
                textStyle = MaterialTheme.typography.bodyMedium
            )



        }

        BigButton(
            label = R.string.continue_,
            enabled = true,
            onClick = {
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Email or Password can't be empty", Toast.LENGTH_SHORT).show()
                }
                else if(password.length < 6 || !email.endsWith("@gmail.com")){
                    Toast.makeText(context, "Email or Password is incorrect!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Signing In...", Toast.LENGTH_SHORT).show()
                    onLoginClick(email, password)
                }
            }
        )
    }

}