package com.presentation.main.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.presentation.design_system.appbar.appbars.LogoUserTopAppBar
import com.presentation.design_system.appbar.appbars.UpBottomBar
import com.presentation.design_system.appbar.appbars.UpTab

@Composable
fun MainScene(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    Box(Modifier
        .fillMaxSize()
        .background(CS.Gray.White)
    ) {

        /* ① TopBar + 콘텐츠만 Scaffold 로 관리 */
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing
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