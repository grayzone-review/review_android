package com.presentation.login

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.login.scenes.login.LoginScene
import com.presentation.login.scenes.login.LoginViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface LoginAPI: FeatureAPI { }

internal object InternalLoginAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.loginSceneRoute,
            route = NavigationRouteConstant.loginNestedRoute
        ) {
            composable(NavigationRouteConstant.loginSceneRoute) {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class LoginAPIImpl: LoginAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalLoginAPI.registerGraph(navController, navGraphBuilder)
    }
}