package com.data.review_android

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.data.review_android.navigation.AppNavGraph
import com.data.review_android.navigation.NavigationProvider
import com.data.review_android.ui.theme.ReviewAndroidTheme
import com.kakao.vectormap.KakaoMapSdk
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import androidx.compose.material3.IconButton as IconButton1

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationProvider: NavigationProvider

    @Inject
    @Named("NATIVE_APP_KEY")
    lateinit var nativeAppKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoMapSdk.init(this, nativeAppKey)

        setContent {
            ReviewAndroidTheme {
                val navController = rememberNavController()
                val appBarViewModel: AppBarViewModel = hiltViewModel()

                Scaffold(
                    topBar = {
                        val appBarState by appBarViewModel.appBarState.collectAsState()
                        DefaultTopAppBar(
                            title = appBarState.title,
                            navigationIcon = {
                                if (appBarState.showBackButton) {
                                    IconButton1(onClick = { navController.navigateUp() }) {
                                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                                    }
                                }
                            },
                            actions = {
                                appBarState.actions.forEach { action ->
                                    IconButton1(onClick = action.onClick) {
                                        Icon(action.icon, action.contentDescription)
                                    }
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    App(
                        navHostController = navController,
                        navigationProvider = navigationProvider,
                        appBarViewModel = appBarViewModel,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}

@Composable
fun App(
    navHostController: NavHostController,
    navigationProvider: NavigationProvider,
    appBarViewModel: AppBarViewModel,
    paddingValues: PaddingValues = PaddingValues()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavGraph(
            navController = navHostController,
            navigationProvider = navigationProvider,
            appBarViewModel = appBarViewModel
        )
    }
}