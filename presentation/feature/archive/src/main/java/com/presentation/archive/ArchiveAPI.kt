package com.presentation.archive
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.presentation.archive.scene.ArchiveScene
import com.presentation.archive.scene.ArchiveViewModel
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import common_ui.FeatureAPI

interface ArchiveAPI: FeatureAPI { }

internal object InternalArchiveAPI: FeatureAPI {
    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = NavigationRouteConstant.archiveSceneRoute,
            route = NavigationRouteConstant.archiveNestedRoute
        ) {
            composable(NavigationRouteConstant.archiveSceneRoute) {
                val viewModel = hiltViewModel<ArchiveViewModel>()
                ArchiveScene(viewModel = viewModel, navController = navController)
            }
        }
    }
}

class ArchiveAPIImpl: ArchiveAPI {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalArchiveAPI.registerGraph(navController, navGraphBuilder)
    }
}