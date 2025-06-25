package com.presentation.login.scenes.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.presentation.designsystem.typography.Typography
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.presentation.design_system.appbar.appbars.AppBarViewModel
import com.team.common.feature_api.utility.Utility
import preset_ui.icons.KakaoBubble

@Composable
fun LoginScene(
    viewModel: LoginViewModel,
    appBarViewModel: AppBarViewModel
) {
    val context = LocalContext.current

    Column(
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        KakaoLoginButton(
            onClick =  {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    Log.d(TAG, "카카오톡 로그인 사용 가능")
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            Log.e(TAG, "카카오톡으로 로그인 실패", error)

                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }
                            Log.e(TAG, "이쪽지점")
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = viewModel.callback)
                        } else if (token != null) {
                            Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        }
                    }
                } else {
                    Log.d(TAG, "카카오톡 로그인 사용 가능2 구간")
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = viewModel.callback)
                }
            }
        )
    }
}

@Composable
fun KakaoLoginButton(
    onClick: () -> Unit
) {
    val kakaoYellow = Utility.hexToColor(hex = "#FEE500")
    val kakaoBrown = Utility.hexToColor(hex = "#3D1D1C")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = kakaoYellow,
                contentColor = kakaoBrown
            ),
        ) {
            KakaoBubble(24.dp, 24.dp)
            Spacer(Modifier.width(8.dp))
            Text(text = "카카오톡으로 시작하기", style = Typography.body1Bold, color = kakaoBrown)
        }
    }
}