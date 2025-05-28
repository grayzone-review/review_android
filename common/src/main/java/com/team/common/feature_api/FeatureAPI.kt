package com.team.common.feature_api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.presentation.design_system.appbar.appbar.AppBarViewModel

interface FeatureAPI {

    fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder,
        appBarViewModel: AppBarViewModel
    )

}