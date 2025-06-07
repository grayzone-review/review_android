package com.feature.comments

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.feature.comments.scene.SearchScene
import com.feature.comments.scene.SearchViewModel
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface SearchAPI: FeatureAPI { }

internal object InternalSearchAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        appBarViewModel: AppBarViewModel
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.searchSceneRoute,
            route = NavigationRouteConstant.searchNestedRoute
        ) {
            composable(NavigationRouteConstant.searchSceneRoute) {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScene(viewModel = viewModel, appBarViewModel = appBarViewModel)
            }
        }
    }
}

class SearchAPIImpl: SearchAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder, appBarViewModel: AppBarViewModel) {
        InternalSearchAPI.registerGraph(
            navController, navGraphBuilder, appBarViewModel
        )
    }
}