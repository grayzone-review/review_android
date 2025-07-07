package com.data.review_android

import BottomSheetHelper
import DimController
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import colors.CS
import com.data.location.UpLocationService
import com.data.review_android.navigation.AppNavGraph
import com.data.review_android.navigation.NavigationProvider
import com.data.review_android.ui.theme.ReviewAndroidTheme
import com.data.storage.datastore.UpDataStoreService
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import com.presentation.design_system.appbar.appbars.DefaultTopAppBar
import dagger.hilt.android.AndroidEntryPoint
import preset_ui.icons.BackBarButtonIcon
import token_storage.TokenStoreService
import javax.inject.Inject
import androidx.compose.material3.IconButton as IconButton1


fun Int.toPx(context: Context): Int =
    (this * context.resources.displayMetrics.density).toInt()

fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        UpDataStoreService.init(context = applicationContext)
        TokenStoreService.init(context = applicationContext)
        UpLocationService.init(context = applicationContext)

        val bottomSheetContainer = findViewById<FrameLayout>(R.id.bottomSheetContainer)
        val dimView = findViewById<FrameLayout>(R.id.dimView)
        val inputBarView = findViewById<ComposeView>(R.id.inputBarView)
        consumeBottomSheetTouchEvent(bottomSheetContainer)
        registerDimViewOnClickListner(dimView)
        val dimController = registerDimController(dimView)
        BottomSheetHelper.init(
            container = bottomSheetContainer,
            dimController = dimController,
            inputBarView = inputBarView
        )

        findViewById<ComposeView>(R.id.composeMain).setContent {
            ReviewAndroidTheme {
                val navController = rememberNavController()
                val appBarViewModel: AppBarViewModel = hiltViewModel()
                val appBarState by appBarViewModel.appBarState.collectAsState()

                Scaffold(
                    topBar = {
                        AnimatedVisibility(
                            visible = appBarState.isVisible,
                            enter = slideInVertically(
                                initialOffsetY = { -it }, // 위에서 아래로 슬라이드 인
                                animationSpec = tween(durationMillis = 300)
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { -it }, // 아래에서 위로 슬라이드 아웃
                                animationSpec = tween(durationMillis = 300)
                            )
                        ) {
                            DefaultTopAppBar(
                                title = appBarState.title,
                                navigationIcon = {
                                    if (appBarState.showBackButton) {
                                        IconButton1(onClick = { navController.navigateUp() }) {
                                            BackBarButtonIcon(width = 24.dp, height = 24.dp, tint = CS.Gray.G90)
                                        }
                                    }
                                },
                                actions = {
                                    appBarState.actions.forEach { action ->
                                        action.icon?.let { icon ->
                                            IconButton1(onClick = action.onClick) {
                                                Icon(icon, action.contentDescription)
                                            }
                                        }
                                    }
                                }
                            )
                        }
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

//    private fun test() {
//        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = LocationRequest.QUALITY_HIGH_ACCURACY
//        }
//
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//
//        val client: SettingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener {
//            // GPS가 켜져있을 경우
//        }
//
//        task.addOnFailureListener { exception ->
//            // GPS가 꺼져있을 경우
//            if (exception is ResolvableApiException) {
//                Log.d(TAG, "OnFailure")
//                try {
//                    exception.startResolutionForResult(
//                        this@MainActivity,
//                        100
//                    )
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    Log.d(TAG, sendEx.message.toString())
//                }
//            }
//        }
//    }

    private fun registerDimViewOnClickListner(dimView: FrameLayout) {
        dimView.setOnClickListener {
            BottomSheetHelper.hide()
        }
    }

    private fun consumeBottomSheetTouchEvent(bottomSheetContainer: FrameLayout) {
        bottomSheetContainer.setOnClickListener { true }
    }

    private fun registerDimController(dimView: FrameLayout): DimController =
        object : DimController {
            override fun showDim() {
                dimView.visibility = View.VISIBLE
            }

            override fun hideDim() {
                dimView.visibility = View.GONE
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