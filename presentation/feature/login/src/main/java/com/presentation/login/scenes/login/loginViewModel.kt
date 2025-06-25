package com.presentation.login.scenes.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidTapKakaoLogin
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    fun handleAction(action: Action, value: Any? = null) = when (action) {
        Action.DidTapKakaoLogin -> { }
    }
}