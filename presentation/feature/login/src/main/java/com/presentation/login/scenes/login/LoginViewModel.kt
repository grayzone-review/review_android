package com.presentation.login.scenes.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginUIState(
    val accessToken: String = "",
    val shoudShowCreateAccountDialog: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        SuccessKakaoLogin,
        FailedKakaoLogin
    }

    private var _uiState = MutableStateFlow(value = LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.SuccessKakaoLogin -> {
                val oAuthToken = value as? OAuthToken ?: return
                _uiState.update {
                    it.copy(
                        accessToken = oAuthToken.accessToken,
                        shoudShowCreateAccountDialog = true)
                }
            }

            Action.FailedKakaoLogin -> {
                Log.e(TAG, "카카오 로그인 실패: $value")
            }
        }
    }
}