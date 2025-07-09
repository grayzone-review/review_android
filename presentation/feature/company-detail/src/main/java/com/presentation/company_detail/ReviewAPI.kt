package com.presentation.company_detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailScene
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface ReviewAPI: FeatureAPI { }

internal object InternalReviewAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.reviewDetailSceneRoute,
            route = NavigationRouteConstant.reviewDetailNestedRoute
        ) {
            composable(NavigationRouteConstant.reviewDetailSceneRoute) {
                val viewModel = hiltViewModel<ReviewDetailViewModel>()
                ReviewDetailScene(viewModel = viewModel)
            }
        }
    }
}

class ReviewAPIImpl: ReviewAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalReviewAPI.registerGraph(navController, navGraphBuilder)
    }
}