package com.presentation.main.scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import create_review_dialog.CreateReviewDialog
import com.presentation.main.scene.MainViewModel.Action.*

@Composable
fun MainScene(
    viewModel: MainViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        when {
            permissionState.allPermissionsGranted -> {
                viewModel.handleAction(GetPopularFeeds)
            }
            permissionState.shouldShowRationale -> {
                viewModel.handleAction(ShowSettingAlert)
            }
            else -> permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { mainUIEvent ->
            when (mainUIEvent) {
                MainUIEvent.ShowSettingAlert -> {
                    context.openAppSettings()
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(CS.Gray.White)
    ) {
        Scaffold(
//            contentWindowInsets = WindowInsets
//                .safeDrawing
//                .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            topBar = {
                LogoUserTopAppBar(
                    userName = "서현웅",
                    onLogoClick = { },
                    onProfileClick = { }
                )
            },
            bottomBar = {
                UpBottomBar(
                    current = UpTab.Home,
                    onTabSelected = { /* TODO */ },
                    onAddButtonClick = { /* TODO */ },
//            modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
                    .padding(inner)
                    .background(Color.White)
            ) {
                TextFieldButton(
                    onClickTextFieldButton = { }
                )
                DashBoardButtons(
                    onSearchClick = {},
                    onMyReviewClick = { },
                    onFollowClick = { }
                )
                LocationBannerButton(
                    onClick = { }
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(CS.Gray.G10))
                RecentReviewSection(
                    reviews = uiState.popularFeeds,
                    onMoreClick = { }
                )
            }
        }
    }
}

@Composable
fun TextFieldButton(
    onClickTextFieldButton: () -> Unit,
) {
    Column(modifier = Modifier.padding(20.dp)) {
        IconTextFieldOutlined(
            text = "재직자들의 리뷰 찾아보기",
            onClick = { }
        )
    }
}

@Composable
fun DashBoardButtons(
    onSearchClick: () -> Unit,
    onMyReviewClick: () -> Unit,
    onFollowClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        DashboardCard(
            modifier = Modifier
                .weight(1f)
                .height(188.dp),
            background = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(CS.PrimaryOrange.O40, RoundedCornerShape(8.dp))
                )
            },
            onClick = onSearchClick
        ) { maxWidth, maxheight ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 7.dp,
                        top = 16.dp,
                        bottom = 20.dp
                    )
                ) {
                    Text("우리 동네 리뷰가\n궁금하다면?", style = Typography.body1Bold, color = CS.Gray.White)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "지금 리뷰 검색하러 가기",
                            style = Typography.captionSemiBold,
                            color = CS.Gray.White
                        )
                        RightArrowIcon(14.dp, 14.dp, tint = CS.Gray.White)
                    }
                }
                MainMapPinIcon(width = maxWidth, height = maxheight * 0.446f)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                onClick = onMyReviewClick
            ) { _, _ ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "내가\n작성한 리뷰",
                        style = Typography.body1Bold,
                        color = CS.Gray.G90,
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .padding(start = 12.dp, top = 16.dp)
                    )
                    Chat2Fill(
                        32.dp, 32.dp, tint = CS.PrimaryOrange.O40, modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 12.dp)
                    )
                }
            }

            DashboardCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                onClick = onFollowClick
            ) { _, _ ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        "팔로우한\n업체",
                        style = Typography.body1Bold,
                        color = CS.Gray.G90,
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .padding(12.dp, 12.dp)
                    )
                    FollowPersonOnIcon(
                        32.dp, 32.dp, modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .padding(end = 8.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit = {
        Box(
            Modifier
                .fillMaxSize()
                .background(CS.Gray.G10, RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = CS.Gray.G20,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    },
    onClick: () -> Unit,
    content: @Composable BoxScope.(Dp, Dp) -> Unit,   // (width,height) 전달
) {
    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        background()
        content(maxWidth, maxHeight)
    }
}

@Composable
private fun LocationBannerButton(
    onClick: () -> Unit,
) {
    BoxWithConstraints(

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { viewModel.handleAction(DidTapOpenSheet)  },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "리뷰-생성")
        }
        
        Button(
            onClick = {  }
        ) {
            Text(text = "리뷰-검색")
        }
    }
}

@Composable
fun CreateReviewDialog(isShow: Boolean, onDismiss: () -> Unit) {
    if (isShow) {
        CreateReviewDialog(
            onDismiss = onDismiss
        )
    }
}