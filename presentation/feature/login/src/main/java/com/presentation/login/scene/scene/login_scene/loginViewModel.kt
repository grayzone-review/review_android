package com.presentation.login.scene.scene.login_scene

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidTapKakaoLogin
    }


    fun handleAction(action: Action, value: Any? = null) = when (action) {
        Action.DidTapKakaoLogin -> { }
    }
}