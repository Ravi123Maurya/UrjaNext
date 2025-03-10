package com.ravimaurya.urjanext.presentation.welcome

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.android.gms.common.util.Strings
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.BigButton
import com.ravimaurya.urjanext.presentation.components.ScreenLaunchAnimation
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.theme.Green40
import com.ravimaurya.urjanext.theme.Green60
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(navController: NavController) {

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(50)
        isVisible = true
    }

    ScreenLaunchAnimation(isVisible) {
//        WelcomeScreenContent(navController)
        WelcomeScreenPager(navController)
    }

}

data class PagerContent(
    val title: Int = -1,
    val text: Int = -1,
    val image: Int = -1,
)

fun getList(): List<PagerContent> {
    return listOf(
        PagerContent(
            title = R.string.page1_title,
            text = R.string.page1_text,
            image = R.drawable.logo2
        ),
        PagerContent(
            title = R.string.page2_title,
            text = R.string.page2_text,
            image = R.drawable.logo2
        ),
        PagerContent(
            title = R.string.page3_title,
            text = R.string.page3_text,
            image = R.drawable.logo2
        )
    )
}


@Composable
fun WelcomeScreenPager(navController: NavController) {

    val context = LocalContext.current

    val linearGradientBG = listOf(
        Color(4, 197, 58),
        Color(209, 255, 77),
        Color.White,
        Color.White,
        Color.White
    )
    val list = getList()
    val pagerState = rememberPagerState(0, 0f, { list.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                alpha = 0.5f,
                brush = Brush.linearGradient(colors = linearGradientBG)
            )
            .padding(vertical = 80.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(){
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                state = pagerState
            ) { currentPage ->

                UrjaPagerContent(list, currentPage)

            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Skip -- Indicator -- Next
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {

                Text(
                    "Skip", style = TextStyle(
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    modifier = Modifier.clickable {
                        val skipPage = pagerState.pageCount - 1
                        scope.launch { pagerState.animateScrollToPage(skipPage) }
                    }
                )

                // Pager Indicator
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    repeat(list.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .width(if (isSelected) 18.dp else 8.dp)
                                .height(if (isSelected) 8.dp else 8.dp)
                                .border(
                                    width = 1.dp,
                                    color = Green60,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = if (isSelected) Green40 else Color(0xFFFFFFFF),
                                    shape = CircleShape
                                )
                        )
                    }
                }


                Text(
                    "Next", style = TextStyle(
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    modifier = Modifier.clickable {
                        if (pagerState.currentPage < 2) {
                            val nextPage = pagerState.currentPage + 1
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    nextPage,
                                    animationSpec = spring()
                                )
                            }
                        }

                    }
                )


            }

            // Continue Button
            BigButton(
                enabled = pagerState.currentPage == list.size - 1,
                label = R.string.continue_,
                onClick = {
                    // Navigate to Authentication
                    navController.navigate(NavRoutes.AUTHENTICATION_SCREEN) {
                        popUpTo(NavRoutes.WELCOME_SCREEN) { inclusive = true }
                    }
                }
            )
        }




    }


}


@Composable
fun WelcomeScreenContent(navController: NavController) {
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

        // Image Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
//                .background(Color.Cyan)
                .padding(top = 40.dp, start = 29.dp, end = 29.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
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
                .height(150.dp)
                .zIndex(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.page3_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.page3_text),
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
                    navController.navigate(NavRoutes.AUTHENTICATION_SCREEN) {
                        popUpTo(NavRoutes.WELCOME_SCREEN) { inclusive = true }
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

@Composable
fun UrjaPagerContent(list: List<PagerContent>, currentPage: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(list[currentPage].image),
            contentDescription = null,
//            contentScale = ContentScale.FillBounds
        )

        // Text Column
        Column(
            modifier = Modifier
                .fillMaxWidth(0.87f)
                .height(150.dp)
                .zIndex(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(list[currentPage].title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(list[currentPage].text),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

        }

    }
}

@Composable
fun PagerIndicator(pages: Int, pagerState: PagerState) {

    repeat(pages) { index ->
        val isSelected = pagerState.currentPage == index
        Box(
            modifier = Modifier
                .padding(4.dp)
                .width(if (isSelected) 18.dp else 8.dp)
                .height(if (isSelected) 8.dp else 8.dp)
                .border(
                    width = 1.dp, color = Color(0xFF707784),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = if (isSelected) Color(0xFF3B6C64)
                    else Color(0xFFFFFFFF), shape = CircleShape
                )
        )
    }
}
