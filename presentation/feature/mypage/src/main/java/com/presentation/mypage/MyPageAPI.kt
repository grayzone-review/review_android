package com.presentation.mypage

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.mypage.scene.MyPageScene
import com.presentation.mypage.scene.MyPageViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface MyPageAPI: FeatureAPI { }

internal object InternalMyPageAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.mypageSceneRoute,
            route = NavigationRouteConstant.mypageNestedRoute
        ) {
            composable(NavigationRouteConstant.mypageSceneRoute) {
                val viewModel = hiltViewModel<MyPageViewModel>()
                MyPageScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class MyPageAPIImpl: MyPageAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalMyPageAPI.registerGraph(navController, navGraphBuilder)
    }
}