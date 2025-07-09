package com.presentation.main.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.presentation.design_system.appbar.appbars.LogoUserTopAppBar

@Composable
fun MainScene(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LogoUserTopAppBar(
            userName = "현웅",
            onLogoClick = { },
            onProfileClick = {  }
        )
    }
}