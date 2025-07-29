package com.presentation.login.scenes.terms

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.presentation.login.scenes.sign_up.navgraph.NavConstant

@Composable
fun TermDetailScene(
    navHostController: NavController
) {
    val url = remember {
        navHostController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>(NavConstant.ARGUMENT_URL)
            .orEmpty()
    }

    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
            }
        }
    }

    key(url) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize(),
            update = { if (url.isNotBlank()) it.loadUrl(url) }
        )
    }

    BackHandler {
        if (webView.canGoBack()) webView.goBack()
        else navHostController.popBackStack()
    }
}
