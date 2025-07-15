package com.presentation.mypage

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.mypage.scene.modify_user.ModifySearchAddressScene
import com.presentation.mypage.scene.modify_user.ModifySearchAddressViewModel
import com.presentation.mypage.scene.modify_user.ModifyUserScene
import com.presentation.mypage.scene.modify_user.ModifyUserViewModel
import com.presentation.mypage.scene.mypage.MyPageScene
import com.presentation.mypage.scene.mypage.MyPageViewModel
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

            composable(NavigationRouteConstant.mypageModifyUserSceneRoute) {
                val viewModel = hiltViewModel<ModifyUserViewModel>()
                ModifyUserScene(viewModel = viewModel, navController = navController)
            }

            composable(MyPageNavRoute.SearchAddress.route) {
                val viewModel = hiltViewModel<ModifySearchAddressViewModel>()
                ModifySearchAddressScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class MyPageAPIImpl: MyPageAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalMyPageAPI.registerGraph(navController, navGraphBuilder)
    }
}