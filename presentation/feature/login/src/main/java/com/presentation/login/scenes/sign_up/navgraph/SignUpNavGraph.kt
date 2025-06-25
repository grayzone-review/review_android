package com.presentation.login.scenes.sign_up.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.presentation.login.scenes.search_address.SearchAddressScene
import com.presentation.login.scenes.sign_up.SignUpScene

sealed class SignUpNavRoute(val route: String) {
    object SignUp : SignUpNavRoute("sign_up")
    object SearchAddress : SignUpNavRoute("search_address")
}

@Composable
fun SignUpNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = SignUpNavRoute.SignUp.route
    ) {
        composable(SignUpNavRoute.SignUp.route) {
            SignUpScene(
                onDismiss = { },
                navHostController = navController
            )
        }
        composable(SignUpNavRoute.SearchAddress.route) {
            SearchAddressScene()
        }
    }
}