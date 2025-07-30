package gps_setting_checker

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import colors.CS
import com.example.presentation.designsystem.typography.Typography
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

@Composable
fun GpsSettingChecker(
    onGpsEnabled: () -> Unit = {},
    onGpsDenied: () -> Unit = {},
) {
    val context = LocalContext.current

    /* 1️⃣ 토스트 메시지 상태 */
    var toastMsg by remember { mutableStateOf<String?>(null) }

    /* 2️⃣ 메시지가 생기면 커스텀 토스트 표시 */
    toastMsg?.let { msg ->
        CustomToast(context).MakeText(
            message = msg,
            duration = Toast.LENGTH_LONG
        )
        toastMsg = null               // 한 번만 띄우고 초기화
    }

    /* 3️⃣ 시스템 다이얼로그 결과 런처 */
    val resolutionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                toastMsg =
                    "위치 설정이 허용되었습니다.\n내 주변 리뷰를 정확히 확인할 수 있어요!"
                onGpsEnabled()
            } else {
                toastMsg = "위치 설정이 거부되었습니다.\n주변 업체·리뷰 기능이 제한됩니다."
                onGpsDenied()
            }
        }

    /* 4️⃣ 첫 진입 시 GPS 상태 확인 */
    LaunchedEffect(Unit) {
        val client = LocationServices.getSettingsClient(context)
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(
                LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    10_000
                ).build()
            ).build()

        runCatching { client.checkLocationSettings(request).await() }
            .onSuccess { onGpsEnabled() }      // 이미 켜져 있으면 토스트 X
            .onFailure { t ->
                if (t is ResolvableApiException) {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(t.resolution).build()
                    resolutionLauncher.launch(intentSenderRequest)
                } else {
                    toastMsg = "위치 설정이 거부되었습니다.\n주변 업체·리뷰 기능이 제한됩니다."
                    onGpsDenied()
                }
            }
    }
}


object CustomToastUtil {

    @Composable
    fun SetView(message: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = CS.Gray.Black,
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message,
                style = Typography.body1Regular,
                color = CS.Gray.White,
                maxLines = 2,
                textAlign = TextAlign.Start
            )
        }
    }
}

class CustomToast(context: Context) : Toast(context) {
    @Composable
    fun MakeText(
        message: String,
        duration: Int = LENGTH_SHORT,
    ) {
        val context = LocalContext.current
        val views = ComposeView(context)

        views.setContent {
            CustomToastUtil.SetView(message = message)
        }
        views.setViewTreeLifecycleOwner(LocalLifecycleOwner.current)
        views.setViewTreeSavedStateRegistryOwner(LocalSavedStateRegistryOwner.current)
        views.setViewTreeViewModelStoreOwner(LocalViewModelStoreOwner.current)

        this.duration = duration
        this.view = views
        this.show()
    }
}