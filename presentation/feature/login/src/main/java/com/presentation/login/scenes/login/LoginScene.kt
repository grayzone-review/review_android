package com.presentation.login.scenes.login

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.presentation.designsystem.typography.Typography
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.presentation.login.scenes.login.LoginViewModel.Action.CompletedSignUp
import com.presentation.login.scenes.login.LoginViewModel.Action.DidTapCloseButton
import com.presentation.login.scenes.login.LoginViewModel.Action.FailedKakaoLogin
import com.presentation.login.scenes.login.LoginViewModel.Action.SuccessKakaoLogin
import com.presentation.login.scenes.sign_up.SignUpRootDialog
import com.team.common.feature_api.utility.Utility
import preset_ui.icons.KakaoBubble

@Composable
fun LoginScene(
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    ResisterCreateAccountDialog(
        accessToken = uiState.accessToken,
        isShow = uiState.shoudShowCreateAccountDialog,
        onDismiss = { viewModel.handleAction(DidTapCloseButton) },
        onSubmitCompleted = { viewModel.handleAction(CompletedSignUp) }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        KakaoLoginButton(
            onClick =  {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }
                            UserApiClient.instance.loginWithKakaoAccount(context) { token2, error2 ->
                                if (error2 != null) {
                                    viewModel.handleAction(FailedKakaoLogin, error2)
                                } else if (token2 != null) {
                                    viewModel.handleAction(SuccessKakaoLogin, token2)
                                }
                            }
                        } else if (token != null) {
                            viewModel.handleAction(SuccessKakaoLogin, token)
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context) { token, error -> if (error != null) {
                            viewModel.handleAction(FailedKakaoLogin, error)
                        } else if (token != null) {
                            viewModel.handleAction(SuccessKakaoLogin, token)
                        }
                    }
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

@Composable
fun ResisterCreateAccountDialog(
    accessToken: String,
    isShow: Boolean,
    onDismiss: () -> Unit,
    onSubmitCompleted: () -> Unit
) {
    if (isShow) SignUpRootDialog(
        accessToken = accessToken,
        onDismiss =  { onDismiss() },
        onSubmitCompleted = { onSubmitCompleted() }
    )
}