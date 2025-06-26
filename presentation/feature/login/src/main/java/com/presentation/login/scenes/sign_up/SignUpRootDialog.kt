package com.presentation.login.scenes.sign_up

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import com.presentation.login.scenes.sign_up.navgraph.SignUpNavGraph

@Composable
fun SignUpRootDialog(onDismiss: () -> Unit) {
    val navController = rememberNavController()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        SignUpNavGraph(navController = navController)
    }
}