package common_ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.presentation.design_system.appbar.appbars.AppBarViewModel

interface FeatureAPI {

    fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        appBarViewModel: AppBarViewModel
    )

}