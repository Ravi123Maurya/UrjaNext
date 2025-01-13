package com.ravimaurya.urjanext.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.domain.model.UserModel
import com.ravimaurya.urjanext.presentation.components.BigButton

@Composable
fun RegistrationContent(onRegisterClick: (String, String, String, String, String) -> Unit){


    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    val list = listOf("Maharashtra", "Uttar Pradesh", "Delhi")
    var selectedItem by remember { mutableStateOf("Select State") }
    var userName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // User Name Field
            Text(
                text = stringResource(R.string.your_name),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.your_name)) },
                value = userName,
                onValueChange = {
                    userName = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = ""
                    )
                },
                shape = ShapeDefaults.ExtraLarge,
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            // Region/State Field
            Text(
                text = stringResource(R.string.select_region),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
                    .clickable { expanded = !expanded },
                label = {},
                value = selectedItem,
                onValueChange = {
                    selectedItem = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Circle,
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if(expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                shape = ShapeDefaults.ExtraLarge,
                textStyle = MaterialTheme.typography.bodyMedium
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                list.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(it)
                        },
                        onClick = {
                            expanded = false
                            selectedItem = it
                        }
                    )
                }
            }

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
                textStyle = MaterialTheme.typography.bodyMedium,
            )

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
                if(userName.isEmpty() || selectedItem == "Select State" || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "All field are required!", Toast.LENGTH_SHORT).show()
                }
                else if(!email.endsWith("@gmail.com")){
                    Toast.makeText(context, "Enter valid email!", Toast.LENGTH_SHORT).show()
                }
                else if(password.length < 6){
                    Toast.makeText(context, "Password should contain least 6 characters!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Signing Up...", Toast.LENGTH_SHORT).show()
                    onRegisterClick(userName, selectedItem, phoneNumber, email, password)
                }
            }
        )
    }


}