package com.presentation.login.scenes.sign_up.navgraph

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.presentation.login.scenes.search_address.SearchAddressScene
import com.presentation.login.scenes.search_address.SearchAddressViewModel
import com.presentation.login.scenes.sign_up.SignUpScene
import com.presentation.login.scenes.sign_up.SignUpViewModel

object NavConstant {
    enum class Mode(val value: String) { MY("my"), INTEREST("interest") }
    const val SIGN_UP_ROUTE = "sign_up"
    private const val SEARCH_ADDRESS_BASE = "search_address"
    private const val ARGUMENT_TOWN = "town"
    private const val ARGUMENT_MODE = "mode"
    const val SEARCH_ADDRESS_ROUTE =
        "$SEARCH_ADDRESS_BASE?$ARGUMENT_TOWN={$ARGUMENT_TOWN}&$ARGUMENT_MODE={$ARGUMENT_MODE}"

    fun destSearchAddress(startQuery: String, mode: Mode) =
        "$SEARCH_ADDRESS_BASE?$ARGUMENT_TOWN=${Uri.encode(startQuery)}&$ARGUMENT_MODE=${mode.value}"
}

sealed class SignUpNavRoute(val route: String) {
    object SignUp : SignUpNavRoute(NavConstant.SIGN_UP_ROUTE)
    object SearchAddress : SignUpNavRoute(NavConstant.SEARCH_ADDRESS_ROUTE)
}

@Composable
fun SignUpNavGraph(
    navController: NavHostController,
    accessToken: String,
    onDismiss: () -> Unit,
    onSubmitCompleted: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = SignUpNavRoute.SignUp.route
    ) {
        composable(SignUpNavRoute.SignUp.route) {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpScene(
                viewModel = viewModel,
                accessToken = accessToken,
                onDismiss = { onDismiss() },
                onSubmitCompleted = { onSubmitCompleted() },
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