package com.presentation.onboarding.scenes.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.presentation.onboarding.scenes.splash.SplashViewModel.Action.OnAppear
import preset_ui.icons.SignInLogo

@Composable
fun SplashScene(
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val systemUiController= rememberSystemUiController()
    val statusBarColor= CS.PrimaryOrange.O40

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor, darkIcons = false)
    }

    DisposableEffect(Unit) {
        onDispose { systemUiController.setStatusBarColor(color = CS.Gray.White, darkIcons = true) }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                SplashUIEvent.NavigateTo -> {
                    navController.navigate(uiState.destination) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleAction(OnAppear)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(CS.PrimaryOrange.O40),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInLogo(100.dp, 100.dp)
        Spacer(Modifier.height(16.dp))
        Text(text = "UP", style = Typography.zenDotsTitle, color = CS.Gray.White)
    }
}