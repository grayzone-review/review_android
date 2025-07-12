package com.presentation.onboarding

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.onboarding.scenes.OnBoardingScene
import com.presentation.onboarding.scenes.OnBoardingViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface OnBoardingAPI: FeatureAPI { }

internal object InternalMainAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.onboardingSceneRoute,
            route = NavigationRouteConstant.onboardingNestedRoute
        ) {
            composable(NavigationRouteConstant.onboardingSceneRoute) {
                val viewModel = hiltViewModel<OnBoardingViewModel>()
                OnBoardingScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class OnBoardingAPIImpl: OnBoardingAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalMainAPI.registerGraph(navController, navGraphBuilder)
    }
}