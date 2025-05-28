package com.presentation.company_detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.company_detail.Scene.ReviewDetailScene
import com.presentation.company_detail.Scene.ReviewDetailViewModel
import com.team.common.feature_api.FeatureAPI
import com.team.common.feature_api.navigation_constant.ReviewDetailFeature

interface ReviewAPI: FeatureAPI { }

internal object InternalReviewAPI: FeatureAPI {
    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = ReviewDetailFeature.reviewDetailSceneRoute,
            route = ReviewDetailFeature.nestedRoute
        ) {
            composable(ReviewDetailFeature.reviewDetailSceneRoute) {
                val viewModel = hiltViewModel<ReviewDetailViewModel>()
                ReviewDetailScene(viewModel = viewModel)
            }
        }
    }
}

class ReviewAPIImpl: ReviewAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalReviewAPI.registerGraph(
            navController, navGraphBuilder
        )
    }

}