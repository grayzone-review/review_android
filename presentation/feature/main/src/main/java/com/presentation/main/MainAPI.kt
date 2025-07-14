package com.presentation.main

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.main.scene.MainScene
import com.presentation.main.scene.MainViewModel
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
        }
    }
}

class MainAPIImpl: MainAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalMainAPI.registerGraph(navController, navGraphBuilder)
    }
}