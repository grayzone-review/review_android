package com.presentation.company_detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.company_detail.Scene.company_detail.CompanyDetailScene
import com.presentation.company_detail.Scene.company_detail.CompanyDetailViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface CompanyDetailAPI: FeatureAPI { }

internal object InternalCompanyDetailAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.reviewDetailSceneRoute,
            route = NavigationRouteConstant.reviewDetailNestedRoute
        ) {
            composable(NavigationRouteConstant.reviewDetailSceneRoute) {
                val viewModel = hiltViewModel<CompanyDetailViewModel>()
                CompanyDetailScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class CompanyDetailAPIImpl: CompanyDetailAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalCompanyDetailAPI.registerGraph(navController, navGraphBuilder)
    }
}