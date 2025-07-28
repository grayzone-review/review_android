package com.presentation.company_detail.Scene.company_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.Company
import com.example.presentation.designsystem.typography.Typography
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.DidCloseBottomSheet
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.DidTapCommentButton
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.DidTapFollowCompanyButton
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.DidTapLikeReviewButton
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.DidTapReviewCard
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.DismissCreateReviewSheet
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.GetReviewsMore
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.OnAppear
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel.Action.ShowCreateReviewSheet
import com.presentation.company_detail.Scene.sheet.CommentBottomSheet
import com.presentation.design_system.R
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.ErrorAction
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.AlertStyle
import common_ui.UpSingleButtonAlertDialog
import create_review_dialog.CreateReviewDialog
import kotlinx.coroutines.flow.distinctUntilChanged
import preset_ui.CSSpacerHorizontal
import preset_ui.IconTextToggleButton
import preset_ui.KakaoMapWithPin
import preset_ui.PrimaryIconTextButton
import preset_ui.ReviewCard
import preset_ui.icons.BackBarButtonIcon
import preset_ui.icons.ChatReviewIcon
import preset_ui.icons.StarFilled

@Composable
fun CompanyDetailScene(
    viewModel: CompanyDetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    var alertError by remember { mutableStateOf<APIException?>(null) }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CompanyDetailUIEvent.ShowAlert -> { alertError = event.error }
            }
        }
    }

    BindAlert(
        error = alertError,
        navController = navController,
        completion = { alertError = null }
    )

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    BackHandler {
        if (uiState.showBottomSheet) {
            viewModel.handleAction(DidCloseBottomSheet)
        } else {
            navController.popBackStack()
        }
    }

    CreateReviewSheet(
        isShow = uiState.shouldShowCreateReviewSheet,
        onDismiss = {
            viewModel.handleAction(DismissCreateReviewSheet)
            viewModel.handleAction(OnAppear)
        }
    )
    Content(viewModel = viewModel, detailUIState = uiState, navController = navController)
    CommentBottomSheet(
        reviewID = uiState.tappedCommentReviewID,
        isShow = uiState.showBottomSheet,
        onDismissRequest = { viewModel.handleAction(DidCloseBottomSheet) }
    )
}

@Composable
fun Content(viewModel: CompanyDetailViewModel, detailUIState: DetailUIState, navController: NavHostController) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            lastVisible >= total - 3 && total > 0
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !detailUIState.isLoading) {
                    viewModel.handleAction(GetReviewsMore)
                }
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = detailUIState.company?.companyName ?: "", onBackButtonClick = { navController.popBackStack() }) },
        containerColor = CS.Gray.White
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                CompanyProfile(
                    company = detailUIState.company,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                StarRating(
                    company = detailUIState.company,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                ProfileActionButtons(
                    company = detailUIState.company,
                    onFollowClick = { viewModel.handleAction(DidTapFollowCompanyButton) },
                    onReviewClick = { viewModel.handleAction(ShowCreateReviewSheet) },
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            item { CompanyLocationMap(
                company = detailUIState.company,
                modifier = Modifier.padding(top = 20.dp))
            }
            item { GraySpacer(Modifier.padding(top = 20.dp)) }

            if (detailUIState.reviews.isEmpty()) {
                item { EmptyCard() }
            } else {
                itemsIndexed(
                    items = detailUIState.reviews
                ) { index, reviewItem ->
                    ReviewCard(
                        review = reviewItem,
                        isFullMode = detailUIState.isFullModeList[index],
                        onReviewCardClick = { viewModel.handleAction(DidTapReviewCard, index) },
                        onLikeReviewButtonClock = { viewModel.handleAction(DidTapLikeReviewButton, index) },
                        onCommentButtonClick = { viewModel.handleAction(DidTapCommentButton, index) },
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    CSSpacerHorizontal(modifier = Modifier, height = 1.dp, color = CS.Gray.G20)
                }
            }
        }
    }
}

@Composable
private fun EmptyCard() {
    val textRes = "아직 등록된 리뷰가 없습니다."
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(206.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ChatReviewIcon(width = 48.dp, height = 48.dp)
        Spacer(Modifier.height(12.dp))
        Text(text = textRes, style = Typography.body1Regular, color = CS.Gray.G50)
    }
}

@Composable
private fun TopAppBar(
    title: String,
    onBackButtonClick: () -> Unit
) {
    DefaultTopAppBar(
        title = title,
        leftNavigationIcon = {
            BackBarButtonIcon(width = 24.dp, height = 24.dp, tint = CS.Gray.G90, modifier = Modifier
                .clickable { onBackButtonClick() })
        }
    )
}

@Composable
fun CompanyProfile(company: Company?, modifier: Modifier) {
    val companyAddress = company?.siteFullAddress
        ?.takeIf { it.isNotEmpty() }
        ?: company?.roadNameAddress?.takeIf { it.isNotEmpty() }
        ?: ""

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = company?.companyName ?: "", color = CS.Gray.G90, style = Typography.h3)
        Text(text = companyAddress, color = CS.Gray.G50, style = Typography.captionRegular)
    }
}

@Composable
fun StarRating(company: Company?, modifier: Modifier) {
    val text = if (company == null) "0.0" else "%.1f".format(company?.totalRating)

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        StarFilled(width = 24.dp, height = 24.dp)
        Text(text = text, color = CS.Gray.G90, style = Typography.h1)
    }
}

@Composable
fun ProfileActionButtons(
    company: Company?,
    onFollowClick: () -> Unit,
    onReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconTextToggleButton(
            text = "팔로우",
            enabled = company?.following ?: false,
            onClick = onFollowClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            iconEnabled = painterResource(R.drawable.following_fill),
            iconDisabled = painterResource(R.drawable.follow_line),
            iconSize = 16.dp,
            spaceBetween = 6.dp
        )

        PrimaryIconTextButton(
            text = "리뷰 작성",
            onClick = onReviewClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            icon = painterResource(R.drawable.pen_fill),
            iconSize = 16.dp,
            spaceBetween = 6.dp,
            cornerRadius = 8.dp
        )
    }
}

@Composable
fun CompanyLocationMap(company: Company?, modifier: Modifier) {
    KakaoMapWithPin(
        modifier = modifier
            .height(179.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        latitude = company?.latitude ?: 37.566535,
        longitude = company?.longitude ?: 126.9779692
    )
}

@Composable
fun GraySpacer(modifier: Modifier) {
    CSSpacerHorizontal(
        modifier = modifier,
        height = 6.dp,
        color = CS.Gray.G20
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