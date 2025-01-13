package com.ravimaurya.urjanext.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.domain.model.UserModel
import com.ravimaurya.urjanext.presentation.components.CircularProgressDialog
import com.ravimaurya.urjanext.presentation.components.ScreenLaunchAnimation
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.theme.Green40
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun AuthPreview(){
    AuthenticationScreen(rememberNavController())
}

@Composable
fun AuthenticationScreen(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
){

    val context = LocalContext.current

    val authState = authenticationViewModel.userAuthState.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(authState.value) {
        when(val state = authState.value){
            is UserAuthenticationState.Error -> {
                isLoading = false
                Toast.makeText(context, "Authentication failed!", Toast.LENGTH_SHORT).show()
            }
            UserAuthenticationState.Loading -> {
                isLoading = true
                println("AuthState: Loading...")
            }
            is UserAuthenticationState.Success -> {
                navController.navigate(NavRoutes.HOME_NAV_GRAPH){
                    popUpTo(NavRoutes.AUTHENTICATION_SCREEN){ inclusive = true }
                }
                isLoading = false
                Toast.makeText(context, "Authentication Successful!", Toast.LENGTH_SHORT).show()
            }
            null -> {
                isLoading = false
            }
        }
    }

    CircularProgressDialog(isLoading)

    ScreenLaunchAnimation(isVisible) {
        AuthenticationContent(
            onRegisterClick = { userName, region, number, email, password ->
                authenticationViewModel.onEvent(AuthenticationEvents.Register(UserModel(email = email, userPassword = password)))
            },
            onLoginClick = { email, password ->
                authenticationViewModel.onEvent(AuthenticationEvents.Login(UserModel(email = email, userPassword = password)))
            }
        )
    }


}

@Composable
fun AuthenticationContent(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String, String, String, String) -> Unit
){
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Logo
                Icon(
                    modifier = Modifier.size(42.dp),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "icon",
                    tint = Green40
                )

                // Welcome to UrjaNext
                Text(
                    text = stringResource(R.string.welcome_to_urjanext),
                    style = MaterialTheme.typography.displaySmall
                )

                // Register and Login TabRow
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    contentColor = Color.Black,
                    containerColor = Color.White,
                    divider = {},
                    indicator = { tabPosition ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPosition[pagerState.currentPage]),
                            height = 2.dp,
                            color = Green40
                        )
                    }
                ) {
                    // Registration Tab
                    Tab(
                        selected = pagerState.currentPage == 0,
                        text = { Text(text = stringResource(R.string.register)) },
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(page = 0) }
                        },

                    )

                    // Login Tab
                    Tab(
                        selected = pagerState.currentPage == 0,
                        text = { Text(text = stringResource(R.string.login)) },
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(page = 1) }
                        }
                    )
                }

                // Registration or Login Content
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false
                ) {
                    when (pagerState.currentPage) {
                        0 -> RegistrationContent(onRegisterClick = { userName, selectedRegion, number, email, password ->
                            onRegisterClick(userName,selectedRegion,number, email,password)
                        })
                        1 -> LoginContent(onLoginClick = { email, password ->
                            onLoginClick(email, password)
                        })
                    }
                }
            }
        }


        // Have an Account?
//            Text(
//                text = stringResource(R.string.have_an_account_login),
//                style = MaterialTheme.typography.bodyMedium
//            )

    }
}






