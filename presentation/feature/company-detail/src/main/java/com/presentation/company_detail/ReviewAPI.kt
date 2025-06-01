package com.presentation.company_detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailScene
import com.presentation.company_detail.Scene.review_detail_scene.ReviewDetailViewModel
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import common_ui.FeatureAPI
import com.team.common.feature_api.navigation_constant.ReviewDetailFeature

interface ReviewAPI: FeatureAPI { }

internal object InternalReviewAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        appBarViewModel: AppBarViewModel
    ) {
        navGraphBuilder.navigation(
            startDestination = ReviewDetailFeature.reviewDetailSceneRoute,
            route = ReviewDetailFeature.nestedRoute
        ) {
            composable(ReviewDetailFeature.reviewDetailSceneRoute) {
                val viewModel = hiltViewModel<ReviewDetailViewModel>()
                ReviewDetailScene(viewModel = viewModel, appBarViewModel = appBarViewModel)
            }
        }
    }
}

class ReviewAPIImpl: ReviewAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder, appBarViewModel: AppBarViewModel) {
        InternalReviewAPI.registerGraph(
            navController, navGraphBuilder, appBarViewModel
        )
    }

}