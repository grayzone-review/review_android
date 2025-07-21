package com.data.review_android

import BottomSheetHelper
import DimController
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.data.location.UpLocationService
import com.data.review_android.navigation.AppNavGraph
import com.data.review_android.navigation.NavigationProvider
import com.data.review_android.ui.theme.ReviewAndroidTheme
import com.data.storage.datastore.UpDataStoreService
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.AndroidEntryPoint
import token_storage.TokenStoreService
import javax.inject.Inject

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

        /* TODO: 제거 */
        UpDataStoreService.lastKnownLocation = "37.5665,126.9780"


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
                App(navHostController = navController, navigationProvider = navigationProvider)
            }
        }
    }

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
) {
    AppNavGraph(
        navController = navHostController,
        navigationProvider = navigationProvider
    )
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