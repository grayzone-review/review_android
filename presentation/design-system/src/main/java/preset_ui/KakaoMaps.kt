package preset_ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.lang.Exception

@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
    locationX: Double, // 서버에서 제공하는 X 값 (경도) latitude
    locationY: Double // 서버에서 제공하는 Y 값 (위도) longitude
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(20.dp)
    val mapView = remember { MapView(context) }
    AndroidView(
        modifier = modifier
            .clip(shape = shape)
            .border(1.dp, CS.Gray.G20, shape),
        factory = { context ->
            mapView.apply {
                mapView.start(
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
                            // 카메라를 (locationY, locationX) 위치로 이동시키는 업데이트 생성
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                LatLng.from(locationY, locationX)
                            )
                            Log.d("안녕시작되었니?", "시작함?")
                            kakaoMap.moveCamera(cameraUpdate)
                        }
                    }
                )
            }
        }
    )

}