package com.data.review_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import androidx.navigation.compose.NavHost
import com.presentation.design_system.appbar.appbars.AppBarViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider,
    appBarViewModel: AppBarViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRouteConstant.loginNestedRoute
    ) {
        navigationProvider.mainAPI.registerGraph(navController, this, appBarViewModel)
        navigationProvider.reviewAPI.registerGraph(navController, this, appBarViewModel)
        navigationProvider.searchAPI.registerGraph(navController, this, appBarViewModel)
        navigationProvider.loginAPI.registerGraph(navController, this, appBarViewModel)
    }
}