package com.presentation.company_detail.Scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.presentation.company_detail.Scene.ReviewDetailViewModel.Action.*
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.presentation.design_system.appbar.appbars.AppBarAction
import com.presentation.design_system.appbar.appbars.AppBarState
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import preset_ui.IconTextToggleButton
import preset_ui.PrimaryIconTextButton

@Composable
fun ReviewDetailScene(
    viewModel: ReviewDetailViewModel,
    appBarViewModel: AppBarViewModel  // 추가
) {
    // 화면 진입 시 AppBar 상태 변경
    LaunchedEffect(Unit) {
        appBarViewModel.updateAppBarState(
            AppBarState(
                title = "리뷰 상세",
                showBackButton = true,
                actions = listOf(
                    AppBarAction(
                        icon = Icons.Default.Edit,
                        contentDescription = "수정",
                        onClick = { /* 수정 액션 */ }
                    )
                )
            )
        )
    }
    Content(viewModel)
}

@Composable
fun Content(viewModel: ReviewDetailViewModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        CompanyProfile(modifier = Modifier.padding(top = 16.dp))
        StarRating(modifier = Modifier.padding(top = 16.dp))
        ProfileActionButtons(
            isFollowing = viewModel.isFollowing,
            onFollowClick = { viewModel.handleAction(DidTapFollowingButton) },
            onReviewClick = {},
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
fun CompanyProfile(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
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
    ) {
        Icon(
            painter = painterResource(com.presentation.design_system.R.drawable.star_fill),
            contentDescription = "별점",
            modifier = Modifier.size(width = 24.dp, height = 24.dp),
            tint = Color.Unspecified
        )
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
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconTextToggleButton(
            text = "팔로우",
            enabled = isFollowing,
            onClick = onFollowClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            iconEnabled = painterResource(com.presentation.design_system.R.drawable.following_fill),
            iconDisabled = painterResource(com.presentation.design_system.R.drawable.follow_line),
            iconSize = 16.dp,
            spaceBetween = 6.dp
        )

        PrimaryIconTextButton(
            text = "리뷰 작성",
            onClick = onReviewClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            icon = painterResource(com.presentation.design_system.R.drawable.pen_fill),
            iconSize = 16.dp,
            spaceBetween = 6.dp,
            cornerRadius = 8.dp
        )
    }
}