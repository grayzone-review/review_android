package com.presentation.main

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.main.scene.feed.FeedScene
import com.presentation.main.scene.feed.FeedViewModel
import com.presentation.main.scene.main.MainScene
import com.presentation.main.scene.main.MainViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface MainAPI: FeatureAPI { }

internal object InternalMainAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.mainSceneRoute,
            route = NavigationRouteConstant.mainNestedRoute
        ) {
            composable(NavigationRouteConstant.mainSceneRoute) {
                val viewModel = hiltViewModel<MainViewModel>()
                MainScene(viewModel = viewModel, navController = navController)
            }

            composable(MainNavRoute.MainFeed.route) {
                val viewModel = hiltViewModel<FeedViewModel>()
                FeedScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class MainAPIImpl: MainAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalMainAPI.registerGraph(navController, navGraphBuilder)
    }
}