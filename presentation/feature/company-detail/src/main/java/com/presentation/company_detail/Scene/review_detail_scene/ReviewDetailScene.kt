package com.presentation.company_detail.Scene.review_detail_scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel.Action.*
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.company_detail.Scene.sheet.CommentBottomSheet
import com.presentation.design_system.R
import com.presentation.design_system.appbar.appbars.AppBarAction
import com.presentation.design_system.appbar.appbars.AppBarState
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import preset_ui.CSSpacerHorizontal
import preset_ui.IconTextToggleButton
import preset_ui.KakaoMapView
import preset_ui.PrimaryIconTextButton
import preset_ui.ReviewCard
import preset_ui.icons.StarFilled

@Composable
fun ReviewDetailScene(
    viewModel: ReviewDetailViewModel,
    appBarViewModel: AppBarViewModel
) {
    // 화면 진입 시 AppBar 상태 변경
    LaunchedEffect(Unit) {
        appBarViewModel.updateAppBarState(
            AppBarState(
                title = "리뷰 상세",
                showBackButton = true,
                actions = listOf(
                    AppBarAction(
                        icon = null,
                        contentDescription = "수정",
                        onClick = { /* 수정 액션 */ }
                    )
                )
            )
        )
    }
    Content(viewModel)
    CommentBottomSheet(viewModel)
}

@Composable
fun Content(viewModel: ReviewDetailViewModel) {
    val listState = rememberLazyListState()
    val localContext = LocalContext.current
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        item { CompanyProfile(Modifier.padding(top = 16.dp)) }
        item { StarRating(Modifier.padding(top = 16.dp)) }
        item {
            ProfileActionButtons(
                isFollowing   = viewModel.isFollowingCompany,
                onFollowClick = { viewModel.handleAction(DidTapFollowCompanyButton) },
                onReviewClick = { viewModel.handleAction(DidTapWriteReviewButton) },
                modifier      = Modifier.padding(top = 20.dp)
            )
        }
        item { CompanyLocationMap(Modifier.padding(top = 20.dp)) }
        item { GraySpacer(Modifier.padding(top = 20.dp)) }
        itemsIndexed(
            items = viewModel.reviews.toList(),
            key   = { _, item -> item.id }
        ) { index, reviewItem ->
            ReviewCard(
                review = reviewItem,
                isFullMode = viewModel.isFullModeList[index],
                onReviewCardClick = { viewModel.handleAction(DidTapReviewCard, index) },
                onLikeReviewButtonClock = { viewModel.handleAction(DidTapLikeReviewButton, index) },
                onCommentButtonClick = { viewModel.handleAction(DidTapCommentButton, index) },
                modifier = Modifier.padding(vertical = 12.dp)
            )
            CSSpacerHorizontal(modifier = Modifier, height = 1.dp, color = CS.Gray.G20)
        }
    }
}

@Composable
fun CompanyProfile(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "스타벅스 석촌역점", color = CS.Gray.G90, style = Typography.h3)
        Text(text = "서울특별시 송파구 백제고분로 358 1층", color = CS.Gray.G50, style = Typography.captionRegular)
    }
}

@Composable
fun StarRating(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        StarFilled(width = 24.dp, height = 24.dp)
        Text(text = "4.5", color = CS.Gray.G90, style = Typography.h1)
    }
}

@Composable
fun ProfileActionButtons(
    isFollowing: Boolean,
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
            enabled = isFollowing,
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
fun CompanyLocationMap(modifier: Modifier) {
    val latitude  = 37.51278
    val longitude = 126.95306

    KakaoMapView(
        modifier = modifier
            .height(179.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        latitude = latitude,
        longitude = longitude
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