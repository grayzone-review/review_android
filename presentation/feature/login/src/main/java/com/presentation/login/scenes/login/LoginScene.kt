package com.presentation.login.scenes.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import colors.CS
import com.data.location.UpLocationService
import com.example.presentation.designsystem.typography.Typography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.presentation.login.scenes.login.LoginViewModel.Action.CacheLocation
import com.presentation.login.scenes.login.LoginViewModel.Action.CompletedSignUp
import com.presentation.login.scenes.login.LoginViewModel.Action.DidTapCloseButton
import com.presentation.login.scenes.login.LoginViewModel.Action.DismissSettingAlert
import com.presentation.login.scenes.login.LoginViewModel.Action.FailedKakaoLogin
import com.presentation.login.scenes.login.LoginViewModel.Action.ShowSettingAlert
import com.presentation.login.scenes.login.LoginViewModel.Action.SuccessKakaoLogin
import com.presentation.login.scenes.sign_up.SignUpRootDialog
import com.team.common.feature_api.extension.openAppSettings
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import com.team.common.feature_api.utility.Utility
import common_ui.UpAlertIconDialog
import preset_ui.icons.KakaoBubble
import preset_ui.icons.MapPinTintable
import preset_ui.icons.SignInLogo

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScene(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsState()
    val permissionState = rememberMultiplePermissionsState(
        UpLocationService.locationPermissions.toList()
    )
    val systemUiController= rememberSystemUiController()
    val statusBarColor= CS.PrimaryOrange.O40

    BackHandler {
        activity?.finishAffinity()
    }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor, darkIcons = false)
    }

    DisposableEffect(Unit) {
        onDispose { systemUiController.setStatusBarColor(color = CS.Gray.White, darkIcons = true) }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        when {
            permissionState.allPermissionsGranted -> {
                viewModel.handleAction(CacheLocation)
            }
            permissionState.shouldShowRationale -> {
                viewModel.handleAction(ShowSettingAlert)
            }
            else -> permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                LoginUIEvent.NavigateToMain -> {
                    navController.navigate(NavigationRouteConstant.mainNestedRoute) {
                        popUpTo(NavigationRouteConstant.loginNestedRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                }

            }
        }
    }

    SettingDialog(
        isShow = uiState.shouldShowSettingAlert,
        onConfirm = { context.openAppSettings() },
        onCancel = { viewModel.handleAction(DismissSettingAlert)}
    )

    ResisterCreateAccountDialog(
        accessToken = uiState.accessToken,
        isShow = uiState.shouldShowCreateAccountDialog,
        onDismiss = { viewModel.handleAction(DidTapCloseButton) },
        onSubmitCompleted = { viewModel.handleAction(CompletedSignUp) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CS.PrimaryOrange.O40)
    ) {
        LogoAndTitle(
            modifier = Modifier.align(Alignment.Center)
        )
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
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
private fun LogoAndTitle(
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInLogo(64.dp, 64.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "현직자들이 말하는\n" + "소규모 사업장 리뷰",
            style = Typography.h1,
            color = CS.Gray.White
        )
    }
}


@Composable
fun KakaoLoginButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    val kakaoYellow = Utility.hexToColor(hex = "#FEE500")
    val kakaoBrown = Utility.hexToColor(hex = "#3D1D1C")
    Column(
        modifier = modifier
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

@Composable
private fun SettingDialog(
    isShow: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (!isShow) return

    UpAlertIconDialog(
        icon = { MapPinTintable(28.dp, 28.dp, tint = CS.Gray.White) },
        title = "위치 권한 필요",
        message = """
            기능을 사용하려면 위치 권한이 필요합니다.
            설정 > 권한에서 위치를 허용해주세요.
        """.trimIndent(),
        confirmText = "설정으로 이동",
        cancelText = "취소",
        onConfirm = onConfirm,
        onCancel = onCancel
    )
}