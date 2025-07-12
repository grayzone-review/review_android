package com.data.review_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRouteConstant.onboardingNestedRoute
    ) {
        navigationProvider.mainAPI.registerGraph(navController, this)
        navigationProvider.reviewAPI.registerGraph(navController, this)
        navigationProvider.searchAPI.registerGraph(navController, this)
        navigationProvider.loginAPI.registerGraph(navController, this)
        navigationProvider.archiveAPI.registerGraph(navController, this)
        navigationProvider.onBoardingAPI.registerGraph(navController, this)
    }
}