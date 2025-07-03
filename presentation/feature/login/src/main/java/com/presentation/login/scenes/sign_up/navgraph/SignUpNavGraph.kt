package com.presentation.login.scenes.sign_up.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.presentation.login.scenes.search_address.SearchAddressScene
import com.presentation.login.scenes.search_address.SearchAddressViewModel
import com.presentation.login.scenes.sign_up.SignUpScene
import com.presentation.login.scenes.sign_up.SignUpViewModel

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
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpScene(
                viewModel = viewModel,
                onDismiss = { },
                navHostController = navController
            )
        }
        composable(SignUpNavRoute.SearchAddress.route) {
            val viewModel = hiltViewModel<SearchAddressViewModel>()
            SearchAddressScene(
                viewModel = viewModel,
                navHostController = navController
            )
        }
    }
}