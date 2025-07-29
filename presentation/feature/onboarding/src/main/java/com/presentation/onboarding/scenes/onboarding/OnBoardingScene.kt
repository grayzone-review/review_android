package com.presentation.onboarding.scenes.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.data.storage.datastore.UpDataStoreService
import com.example.presentation.designsystem.typography.Typography
import com.team.common.feature_api.extension.screenHeightDp
import com.team.common.feature_api.extension.screenWidthDp
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import kotlinx.coroutines.launch
import preset_ui.icons.OnboardFirst
import preset_ui.icons.OnboardSecond
import preset_ui.icons.OnboardThird

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScene(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val screenWidth = context.screenWidthDp.toDouble()
    val screenHeight = context.screenHeightDp.toDouble()

    val pages: List<@Composable () -> Unit> = listOf(
        { IntroPageFirst((screenWidth * 0.722f).dp, (screenHeight * 0.651f).dp) },
        { IntroPageSecond((screenWidth * 0.889f).dp, (screenHeight * 0.48f).dp) },
        { IntroPageThird((screenWidth * 0.806f).dp, (screenHeight * 0.618f).dp) }
    )

    val pagerState = rememberPagerState(pageCount = { pages.size }, initialPage = 0)

    Column(Modifier.fillMaxSize()) {
        IntroPager(
            pages = pages,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        PagerActionButton(
            pagerState = pagerState,
            onFinishClick = {
                UpDataStoreService.needOnBoardingScene = false
                navController.navigate(NavigationRouteConstant.loginSceneRoute) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun IntroPager(
    pages: List<@Composable () -> Unit>,
    pagerState: PagerState,
    modifier: Modifier = Modifier   // 추가
) {
    Column(
        modifier = modifier.background(CS.Gray.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            pages.indices.forEach { index ->
                val selected = pagerState.currentPage == index
                Box(
                    Modifier
                        .size(8.dp)
                        .background(
                            if (selected) CS.PrimaryOrange.O40 else CS.Gray.G20,
                            CircleShape
                        )
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            beyondViewportPageCount = 1
        ) { page ->
            pages[page]()
        }
    }
}

@Composable
private fun IntroPageFirst(imageWidth: Dp, imageHeight: Dp) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text(text = "소규모 사업장에서의 근무", style = Typography.h1Regular, color = CS.Gray.G90)
        Spacer(Modifier.height(4.dp))
        Text(text = "전·현직자들의 이야기를 들어보세요", style = Typography.h1, color = CS.Gray.G90)
        Spacer(modifier = Modifier.weight(1f))
        OnboardFirst(width = imageWidth, height = imageHeight)
    }
}

@Composable
fun IntroPageSecond(imageWidth: Dp, imageHeight: Dp) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text(text = "400만개의 전국 사업장에서", style = Typography.h1Regular, color = CS.Gray.G90)
        Spacer(Modifier.height(4.dp))
        Text(text = "실제 근무 경험이 공유되고 있어요", style = Typography.h1, color = CS.Gray.G90)
        Spacer(modifier = Modifier.height(40.dp))
        OnboardSecond(width = imageWidth, height = imageHeight)
    }
}

@Composable
fun IntroPageThird(imageWidth: Dp, imageHeight: Dp) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text(text = "직접 경험한 리뷰", style = Typography.h1Regular, color = CS.Gray.G90)
        Spacer(Modifier.height(4.dp))
        Text(text = "지금, 익명으로 남겨보세요!", style = Typography.h1, color = CS.Gray.G90)
        Spacer(modifier = Modifier.weight(1f))
        OnboardThird(width = imageWidth, height = imageHeight)
    }
}

@Composable
private fun PagerActionButton(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onFinishClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pagerState.pageCount - 1

    PrimaryWideButton(
        modifier = modifier,
        text = if (isLast) "시작하기" else "다음",
        onClick = {
            scope.launch {
                if (isLast) onFinishClick()
                else pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    )
}

@Composable
private fun PrimaryWideButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(CS.PrimaryOrange.O40)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = Typography.body1Bold, color = CS.Gray.White)
    }
}