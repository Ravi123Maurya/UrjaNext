package com.ravimaurya.urjanext.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.BigButton
import com.ravimaurya.urjanext.presentation.components.ScreenLaunchAnimation
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.theme.Green40
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController){

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(50)
        isVisible = true
    }

    ScreenLaunchAnimation(isVisible) {
        WelcomeScreenContent(navController)
    }
   
}

@Composable
fun WelcomeScreenContent(navController: NavController){
    val linearGradientBG = listOf(
        Color(4, 197, 58),
        Color(209, 255, 77),
        Color.White,
        Color.White,
        Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                alpha = 0.5f,
                brush = Brush.linearGradient(colors = linearGradientBG)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight(0.6f)
//                .background(Color.Cyan)
                .padding( top = 40.dp, start = 29.dp, end = 29.dp)
            ,
            contentAlignment = Alignment.BottomCenter
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                ,
            ){
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.iphone),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    painter = painterResource(R.drawable.ev_charging),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }


        }


        // Text Column
        Column(
            modifier = Modifier
                .fillMaxWidth(0.87f)
                .height( 150.dp).zIndex(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.track_charging),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.lorem_ipsum),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

        }

        // Button Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BigButton(
                label = R.string.continue_,
                onClick = {
                    // Navigate to Authentication
                    navController.navigate(NavRoutes.AUTHENTICATION_SCREEN){
                        popUpTo(NavRoutes.WELCOME_SCREEN){ inclusive = true }
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.learn_more),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Green40
                )
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "",
                    tint = Green40
                )
            }
        }



    }
}

