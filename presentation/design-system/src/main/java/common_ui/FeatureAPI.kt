package common_ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureAPI {
    fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    )
}