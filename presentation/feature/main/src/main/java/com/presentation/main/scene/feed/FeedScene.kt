package com.presentation.main.scene.feed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.CompactCompany
import com.domain.entity.Review
import com.domain.entity.ReviewFeed
import com.domain.entity.User
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.main.NavConstant
import com.presentation.main.scene.feed.FeedViewModel.Action.DidTapCommentButton
import com.presentation.main.scene.feed.FeedViewModel.Action.DidTapLikeReviewButton
import com.presentation.main.scene.feed.FeedViewModel.Action.DismissCommentBottomSheet
import com.presentation.main.scene.feed.FeedViewModel.Action.DismissCrateReviewSheet
import com.presentation.main.scene.feed.FeedViewModel.Action.GetMoreFeeds
import com.presentation.main.scene.feed.FeedViewModel.Action.OnAppear
import com.presentation.main.scene.feed.FeedViewModel.Action.ShowCreateReviewSheet
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.ErrorAction
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import com.team.common.feature_api.utility.Utility
import comment_bottom_sheet.CommentBottomSheet
import common_ui.AlertStyle
import common_ui.UpSingleButtonAlertDialog
import create_review_dialog.CreateReviewDialog
import kotlinx.coroutines.flow.distinctUntilChanged
import preset_ui.ReviewCard
import preset_ui.icons.BackBarButtonIcon
import preset_ui.icons.StarFilled
import preset_ui.icons.StarHalf
import preset_ui.icons.StarOutline

@Composable
fun FeedScene(
    viewModel: FeedViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    var alertError by remember { mutableStateOf<APIException?>(null) }

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is FeedUIEvent.ShowAlert -> { alertError = event.error }
            }
        }
    }

    BindAlert(
        error = alertError,
        navController = navController,
        completion = { alertError = null }
    )

    CommentBottomSheet(
        reviewID = uiState.commentTargetFeed?.review?.id ?: 0,
        isShow = uiState.shouldShowCommentBottomSheet,
        onDismissRequest = { viewModel.handleAction(DismissCommentBottomSheet) }
    )
    CreateReviewSheet(isShow = uiState.shouldShowCreateReviewSheet, onDismiss = { viewModel.handleAction(DismissCrateReviewSheet) })

    BackHandler {
        when {
            uiState.shouldShowCommentBottomSheet -> { viewModel.handleAction(DismissCommentBottomSheet) }
            uiState.shouldShowCreateReviewSheet -> { viewModel.handleAction(DismissCrateReviewSheet) }
            else -> { navController.popBackStack() }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(section = uiState.section, user = uiState.user, onBackButtonClick = { navController.popBackStack() })
        },
        bottomBar = { WriteReviewButton(onClick = { viewModel.handleAction(ShowCreateReviewSheet) }) },
        containerColor = CS.Gray.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ReviewFeedList(
                reviewFeeds = uiState.reviews,
                companyFeeds = uiState.companies,
                onClickCompanyCard = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                    .replace("{companyId}", it.id.toString()))
                },
                onClickReviewCard = { navController.navigate(NavigationRouteConstant.reviewDetailSceneRoute
                    .replace("{companyId}", it.compactCompany.id.toString()))
                },
                onLikeReviewButtonClock = { viewModel.handleAction(DidTapLikeReviewButton, it.id) },
                onCommentButtonClick = { viewModel.handleAction(DidTapCommentButton, it) },
                onLoadMore = { viewModel.handleAction(GetMoreFeeds) }
            )
        }
    }
}

@Composable
private fun TopAppBar(
    section: String,
    user: User,
    onBackButtonClick: () -> Unit
) {
    val dong = user.mainRegion?.name
        ?.split(" ")
        ?.getOrNull(2)
        ?: ""

    val titleText = when (section) {
        NavConstant.Section.MyTown.value           -> "$dong 최근 리뷰"
        NavConstant.Section.InterestRegions.value  -> "관심 지역 최근 리뷰"
        NavConstant.Section.Popular.value          -> "최근 인기 리뷰"
        else -> { "리뷰" }
    }

    DefaultTopAppBar(
        title = titleText,
        leftNavigationIcon = {
            BackBarButtonIcon(width = 24.dp, height = 24.dp, tint = CS.Gray.G90, modifier = Modifier
                .clickable { onBackButtonClick() })
        }
    )
}

@Composable
private fun ReviewFeedList(
    reviewFeeds: List<ReviewFeed>,
    companyFeeds: List<ReviewFeed>,
    onClickCompanyCard: (CompactCompany) -> Unit,
    onClickReviewCard: (ReviewFeed) -> Unit,
    onLikeReviewButtonClock: (Review) -> Unit,
    onCommentButtonClick: (ReviewFeed) -> Unit,
    onLoadMore: () -> Unit
) {
    val itemCount = maxOf(companyFeeds.size * 3, reviewFeeds.size + (reviewFeeds.size / 2))
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex != null && lastVisibleItemIndex >= itemCount - 5) {
                    onLoadMore()
                }
            }
    }

    LazyColumn {
        items(itemCount) { index ->
            when {
                index % 3 == 0 -> {
                    val companyIndex = index / 3
                    val reviewIndexStart = companyIndex * 2
                    val hasReviews = reviewFeeds.getOrNull(reviewIndexStart) != null

                    if (hasReviews) {
                        companyFeeds.getOrNull(companyIndex)?.let { companyFeed ->
                            CompanyCard(
                                company = companyFeed.compactCompany,
                                onClick = { onClickCompanyCard(companyFeed.compactCompany) }
                            )
                        }
                    }
                }
                else -> {
                    val reviewIndex = index - (index / 3) - 1
                    reviewFeeds.getOrNull(reviewIndex)?.let { reviewFeed ->
                        ReviewCard(
                            review = reviewFeed.review,
                            isFullMode = true,
                            modifier = Modifier.fillMaxWidth(),
                            onReviewCardClick = { onClickReviewCard(reviewFeed) },
                            onLikeReviewButtonClock = { onLikeReviewButtonClock(reviewFeed.review) },
                            onCommentButtonClick = { onCommentButtonClick(reviewFeed) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanyCard(
    company: CompactCompany,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .border(width = 1.dp, color = CS.Gray.G20, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = company.companyName,
            style = Typography.body1Bold,
            color = CS.Gray.G90,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 업종이 CompactCompany에 없다면 하드코딩(예: 음식점)
            Text("음식점", style = Typography.captionRegular, color = CS.Gray.G50)

            Spacer(
                Modifier
                    .width(1.dp)
                    .height(18.dp)
                    .background(CS.Gray.G20)
            )

            Text(
                text = company.companyAddress,
                style = Typography.captionRegular,
                color = CS.Gray.G50,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(16.dp))

        /* 3) 평점(숫자 + 별) */
        val starCounts = Utility.calculateStarCounts(totalScore = company.totalRating)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "%.1f".format(company.totalRating),
                style = Typography.h3,
                color = CS.Gray.G90
            )
            Spacer(Modifier.width(8.dp))
            repeat(starCounts.full) { StarFilled(20.dp, 20.dp) }
            repeat(starCounts.half) { StarHalf(20.dp, 20.dp) }
            repeat(starCounts.empty) { StarOutline(20.dp, 20.dp) }
        }

        Spacer(Modifier.height(20.dp))

        /* 4) 한줄평 배지 + 내용 */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .background(CS.Gray.G10, RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("한줄평", style = Typography.captionSemiBold, color = CS.Gray.G90)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = company.reviewTitle ?: "",
                style = Typography.captionRegular,
                color = CS.Gray.G90,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun WriteReviewButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp, top = 5.dp)
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CS.PrimaryOrange.O40,
            contentColor = CS.Gray.White,
            disabledContainerColor = CS.PrimaryOrange.O20,
            disabledContentColor = CS.Gray.White
        ),
        elevation = null
    ) {
        Text(text = "리뷰 작성하러 가기", style = Typography.body1Bold)
    }
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