package com.presentation.main.scene.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import colors.CS
import com.data.location.UpLocationService
import com.domain.entity.ReviewFeed
import com.example.presentation.designsystem.typography.Typography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.presentation.design_system.appbar.appbars.LogoUserTopAppBar
import com.presentation.design_system.appbar.appbars.UpBottomBar
import com.presentation.design_system.appbar.appbars.UpTab
import com.presentation.main.NavConstant
import com.presentation.main.scene.feed.BindAlert
import com.presentation.main.scene.main.MainViewModel.Action.CacheLocationAndGetFeeds
import com.presentation.main.scene.main.MainViewModel.Action.DismissCreateReviewSheet
import com.presentation.main.scene.main.MainViewModel.Action.DismissSettingAlert
import com.presentation.main.scene.main.MainViewModel.Action.OnAppear
import com.presentation.main.scene.main.MainViewModel.Action.ShowCreateReviewSheet
import com.presentation.main.scene.main.MainViewModel.Action.ShowSettingAlert
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.ErrorAction
import com.team.common.feature_api.extension.openAppSettings
import com.team.common.feature_api.extension.screenWidthDp
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.AlertStyle
import common_ui.UpAlertIconDialog
import common_ui.UpSingleButtonAlertDialog
import create_review_dialog.CreateReviewDialog
import gps_setting_checker.GpsSettingChecker
import kotlinx.coroutines.launch
import preset_ui.IconTextFieldOutlined
import preset_ui.icons.Chat2Fill
import preset_ui.icons.FollowPersonOnIcon
import preset_ui.icons.LocationBanner
import preset_ui.icons.MainMapPinIcon
import preset_ui.icons.MapPinTintable
import preset_ui.icons.RightArrowIcon
import preset_ui.icons.StarFilled

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScene(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val permissionState = rememberMultiplePermissionsState(UpLocationService.locationPermissions.toList())
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var alertError by remember { mutableStateOf<APIException?>(null) }

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MainUIEvent.ShowAlert -> { alertError = event.error }
            }
        }
    }

    GpsSettingChecker()
    LaunchedEffect(permissionState.allPermissionsGranted) {
        when {
            permissionState.allPermissionsGranted -> {
                viewModel.handleAction(CacheLocationAndGetFeeds)
            }
            permissionState.shouldShowRationale -> {
                viewModel.handleAction(ShowSettingAlert)
            }
            else -> permissionState.launchMultiplePermissionRequest()
        }
    }

    BindAlert(
        error = alertError,
        navController = navController,
        completion = { alertError = null }
    )

    CreateReviewSheet(
        isShow = uiState.shouldShowCreateReviewSheet,
        onDismiss = { viewModel.handleAction(DismissCreateReviewSheet) }
    )

    SettingDialog(
        isShow = uiState.isShowSettingAlertDialog,
        onConfirm = { context.openAppSettings() },
        onCancel = {
            viewModel.handleAction(DismissSettingAlert)
            viewModel.handleAction(CacheLocationAndGetFeeds)
        }
    )
    
    Box(
        Modifier
            .fillMaxSize()
            .background(CS.Gray.White)
    ) {
        Scaffold(
            topBar = {
                LogoUserTopAppBar(
                    userName = "${uiState.user.nickname}님",
                    onLogoClick = { scope.launch { scrollState.animateScrollTo(value = 0) } },
                    onProfileClick = { navController.navigate(NavigationRouteConstant.archiveNestedRoute) }
                )
            },
            bottomBar = {
                UpBottomBar(
                    current = UpTab.Home,
                    onTabSelected = { tab ->
                        when (tab) {
                            UpTab.Home -> {
                                scope.launch { scrollState.animateScrollTo(value = 0) }
                            }
                            UpTab.MyPage -> {
                                navController.navigate(NavigationRouteConstant.mypageNestedRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    },
                    onAddButtonClick = { viewModel.handleAction(ShowCreateReviewSheet) },
                )
            },
            containerColor = CS.Gray.White
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
                    .padding(inner)
                    .background(Color.White)
            ) {
                TextFieldButton(
                    onClickTextFieldButton = { navController.navigate(NavigationRouteConstant.searchNestedRoute) }
                )
                DashBoardButtons(
                    onSearchClick = { navController.navigate(NavigationRouteConstant.searchNestedRoute) },
                    onMyReviewClick = { navController.navigate(NavigationRouteConstant.archiveNestedRoute) },
                    onFollowClick = { navController.navigate(NavigationRouteConstant.archiveNestedRoute) }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
                if (uiState.user.interestedRegions.isNullOrEmpty()) {
                    LocationBannerButton(
                        onClick = { navController.navigate(NavigationRouteConstant.mypageModifyUserSceneRoute) }
                    )
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(CS.Gray.G10))
                ReviewSection(
                    title = "지금 인기 있는 리뷰",
                    reviews = uiState.popularFeeds,
                    onMoreClick = { navController.navigate(NavConstant
                        .destFeed(section = NavConstant.Section.Popular))
                    },
                    onClickItem = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                        .replace("{companyId}", it.compactCompany.id.toString())) }
                )
                if (uiState.myTownFeeds.isNotEmpty()) {
                    ReviewSection(
                        title = "우리 동네 최근 리뷰",
                        reviews = uiState.myTownFeeds,
                        onMoreClick = { navController.navigate(NavConstant
                            .destFeed(section = NavConstant.Section.MyTown))
                        },
                        onClickItem = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                            .replace("{companyId}", it.compactCompany.id.toString())) }
                    )
                }
                if (uiState.interestRegionsFeeds.isNotEmpty()) {
                    ReviewSection(
                        title = "관심 동네 최근 리뷰",
                        reviews = uiState.interestRegionsFeeds,
                        onMoreClick = { navController.navigate(NavConstant
                            .destFeed(section = NavConstant.Section.InterestRegions))
                        },
                        onClickItem = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                            .replace("{companyId}", it.compactCompany.id.toString())) }
                    )
                }
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
            onClick = onClickTextFieldButton
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
        modifier = Modifier
            .fillMaxWidth()
            .height(142.dp)
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {

        LocationBanner(
            width = maxWidth * 0.34f, height = maxHeight * 0.86f, modifier = Modifier
                .align(Alignment.CenterEnd)
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "관심 동네 선택하러 가기",
                    style = Typography.body1Bold,
                    color = CS.Gray.G90
                )
                Spacer(Modifier.width(4.dp))
                RightArrowIcon(20.dp, 20.dp, tint = CS.Gray.G90)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "관심 동네 선택하고 리뷰 확인해보세요!",
                style = Typography.captionRegular,
                color = CS.Gray.G50
            )
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    reviews: List<ReviewFeed>,
    onMoreClick: () -> Unit,
    onClickItem: (ReviewFeed) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 5.dp)
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = title, style = Typography.h3, color = CS.Gray.G90)
                Spacer(Modifier.width(4.dp))
                Chat2Fill(20.dp, 20.dp, tint = CS.PrimaryOrange.O40)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onMoreClick) {
                Text(text = "더보기", style = Typography.captionRegular, color = CS.Gray.G50)
                RightArrowIcon(14.dp, 14.dp, tint = CS.Gray.G50)
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { item ->
                val cardWidth = LocalContext.current.screenWidthDp * 0.861
                ReviewCard(reviewFeed = item, modifier = Modifier
                    .width(cardWidth.dp)
                    .height(186.dp)
                    .clickable { onClickItem(item) }
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(
    reviewFeed: ReviewFeed,
    modifier: Modifier = Modifier,
) {
    val review = reviewFeed.review
    val company = reviewFeed.compactCompany

    Column(
        modifier = modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = CS.Gray.G20,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        /* 제목 + 별점 */
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = review.title ?: "",
                style = Typography.body1Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            StarFilled(24.dp, 24.dp, modifier = Modifier.offset(y = -(2).dp))
            Spacer(Modifier.width(4.dp))
            Text(text = "%.1f".format(company.totalRating), style = Typography.body1Bold, color = CS.Gray.G80)
        }
        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            text = review.advantagePoint ?: "",
            style = Typography.captionRegular,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = company.companyName, style = Typography.captionRegular, color = CS.Gray.G50)
            Text(
                text = review.createdAt?.substring(0, 7)?.replace("-", ".") + " 작성",
                style = Typography.captionRegular,
                color = CS.Gray.G50
            )
        }
    }
}

@Composable
private fun SettingDialog(
    isShow: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (!isShow) return

    UpAlertIconDialog(
        icon = { MapPinTintable(28.dp, 28.dp, tint = CS.Gray.White) },
        title = "위치 권한 필요",
        message = """
            기능을 사용하려면 위치 권한이 필요합니다.
            설정 > 권한에서 위치를 허용해주세요.
        """.trimIndent(),
        confirmText = "설정으로 이동",
        cancelText = "취소",
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}

@Composable
private fun CreateReviewSheet(isShow: Boolean, onDismiss: () -> Unit) {
    if (isShow) { CreateReviewDialog(onDismiss = onDismiss) }
}

@Composable
fun BindAlert(
    error: APIException?,
    navController: NavController,
    completion: () -> Unit
) {
    error?.let {
        UpSingleButtonAlertDialog(
            message = it.message,
            style = AlertStyle.Error,
            onDismiss = {
                when (it.action) {
                    ErrorAction.Home -> navController.navigate(NavigationRouteConstant.mainNestedRoute)
                    ErrorAction.Login -> navController.navigate(NavigationRouteConstant.loginNestedRoute) {
                        popUpTo(NavigationRouteConstant.mainNestedRoute) { inclusive = true }
                    }
                    ErrorAction.Back -> navController.popBackStack()
                    else -> {}
                }
                completion()
            }
        )
    }
}