package com.presentation.login.scenes.terms

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun TermDetailScene(
    navHostController: NavController,
    viewModel: TermDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val webView = remember {
        WebView.setWebContentsDebuggingEnabled(true)

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
    BackHandler {
        if (webView.canGoBack()) webView.goBack()
        else navHostController.popBackStack()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize(),
            update = { if (uiState.url.isNotBlank()) it.loadUrl(uiState.url) }
        )
    }
}
