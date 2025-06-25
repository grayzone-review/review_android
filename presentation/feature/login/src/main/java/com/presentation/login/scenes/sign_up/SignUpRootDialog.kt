package com.presentation.login.scenes.sign_up

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.presentation.login.scenes.sign_up.navgraph.SignUpNavGraph

@Composable
fun SignUpRootDialog() {
    val navController = rememberNavController()
    Surface { SignUpNavGraph(navController = navController) }
}