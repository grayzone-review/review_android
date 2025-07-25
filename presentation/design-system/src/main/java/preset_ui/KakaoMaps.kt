package preset_ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import colors.CS
import com.kakao.vectormap.BuildConfig
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import preset_ui.icons.MapPinTintable


@Composable
fun KakaoMapWithPin(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .border(1.dp, CS.Gray.G20, shape)
    ) {
        KakaoMapView(
            modifier = Modifier.fillMaxSize(),
            latitude = latitude,
            longitude = longitude
        )

        MapPinTintable(width = 48.dp, height = 48.dp, tint = CS.PrimaryOrange.O40, Modifier
            .align(Alignment.Center)
            .offset(y = -(20).dp)
        )
    }
}

@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
    latitude: Double,   // 위도
    longitude: Double   // 경도
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(20.dp)

    key(latitude, longitude) {
        val mapView = remember { MapView(context) }

        AndroidView(
            modifier = modifier
                .clip(shape)
                .border(1.dp, CS.Gray.G20, shape)
                .pointerInput(Unit) {
                    awaitPointerEventScope { while (true) { awaitPointerEvent().changes.forEach { it.consume() } } }
                },
            factory = {
                mapView.apply {
                    start(
                        object : MapLifeCycleCallback() {
                            override fun onMapDestroy() {
                                if (BuildConfig.DEBUG) {
                                    throw AssertionError("MapLifeCycleCallback.onMapDestroy() 호출됨!")
                                }
                            }
                            override fun onMapError(exception: Exception) {
                                if (BuildConfig.DEBUG) {
                                    throw AssertionError("MapLifeCycleCallback.onMapError() 호출됨, exception=$exception")
                                }
                            }
                        },
                        object : KakaoMapReadyCallback() {
                            override fun onMapReady(kakaoMap: KakaoMap) {
                                kakaoMap.moveCamera(
                                    CameraUpdateFactory.newCenterPosition(
                                        LatLng.from(latitude, longitude)
                                    )
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}
