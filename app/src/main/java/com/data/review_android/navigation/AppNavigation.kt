package com.data.review_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.team.common.feature_api.navigation_constant.ReviewDetailFeature
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider
) {
    NavHost(
        navController = navController,
        startDestination = ReviewDetailFeature.nestedRoute
    ) {
        navigationProvider.reviewAPI.registerGraph(
            navController = navController,
            navGraphBuilder = this
        )
    }
}