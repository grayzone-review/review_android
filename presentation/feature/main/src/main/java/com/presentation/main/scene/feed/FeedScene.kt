package com.presentation.main.scene.feed

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.CompactCompany
import com.domain.entity.ReviewFeed
import com.domain.entity.User
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.main.NavConstant
import com.presentation.main.scene.feed.FeedViewModel.Action.OnAppear
import com.team.common.feature_api.utility.Utility
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

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                section = uiState.section, user = uiState.user, onBackButtonClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ReviewFeedList(reviewFeeds = uiState.reviewFeeds)
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
private fun ReviewFeedList(reviewFeeds: List<ReviewFeed>) {
    LazyColumn {
        items(reviewFeeds.chunked(3)) { chunkedFeed ->
            chunkedFeed.getOrNull(0)?.let { companyFeed ->
                CompanyCard(
                    company = companyFeed.compactCompany,
                    onClick = { /* TODO */ }
                )
            }

            chunkedFeed.drop(1).forEach { reviewFeed ->
                ReviewCard(
                    review = reviewFeed.review,
                    isFullMode = true,
                    modifier = Modifier.fillMaxWidth(),
                    onReviewCardClick = { /* TODO */ },
                    onLikeReviewButtonClock = { /* TODO */ },
                    onCommentButtonClick = { }
                )
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