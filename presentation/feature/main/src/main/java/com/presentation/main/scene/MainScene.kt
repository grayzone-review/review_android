package com.presentation.main.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.LogoUserTopAppBar
import com.presentation.design_system.appbar.appbars.UpBottomBar
import com.presentation.design_system.appbar.appbars.UpTab
import preset_ui.IconTextFieldOutlined
import preset_ui.icons.Chat2Fill
import preset_ui.icons.FollowPersonOnIcon
import preset_ui.icons.MainMapPinIcon
import preset_ui.icons.RightArrowIcon

@Composable
fun MainScene(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .background(CS.Gray.White)
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets
                .safeDrawing
                .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            topBar = {
                LogoUserTopAppBar(
                    userName = "서현웅",
                    onLogoClick = { },
                    onProfileClick = { }
                )
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
            }
        }

        UpBottomBar(
            current = UpTab.Home,
            onTabSelected = { /* TODO */ },
            onAddButtonClick = { /* TODO */ },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
    onFollowClick: () -> Unit
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
                    modifier = Modifier.padding(start = 16.dp, end = 7.dp, top = 16.dp, bottom = 20.dp)
                ) {
                    Text("우리 동네 리뷰가\n궁금하다면?", style = Typography.body1Bold, color = CS.Gray.White)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("지금 리뷰 검색하러 가기", style = Typography.captionSemiBold, color = CS.Gray.White)
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
                    Text(text = "내가\n작성한 리뷰", style = Typography.body1Bold, color = CS.Gray.G90, modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .padding(start = 12.dp, top = 16.dp)
                    )
                    Chat2Fill(32.dp, 32.dp, tint = CS.PrimaryOrange.O40, modifier = Modifier
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
                    Text("팔로우한\n업체", style = Typography.body1Bold, color = CS.Gray.G90, modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .padding(12.dp, 12.dp)
                    )
                    FollowPersonOnIcon(32.dp, 32.dp, modifier = Modifier
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