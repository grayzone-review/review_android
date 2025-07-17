package com.presentation.main.scene.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.domain.entity.User
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import com.presentation.main.NavConstant
import com.presentation.main.scene.feed.FeedViewModel.Action.GetFeeds
import com.presentation.main.scene.feed.FeedViewModel.Action.GetUser
import preset_ui.icons.BackBarButtonIcon

@Composable
fun FeedScene(
    viewModel: FeedViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleAction(GetFeeds)
        viewModel.handleAction(GetUser)
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
            Text(text = uiState.reviewFeeds.size.toString())
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