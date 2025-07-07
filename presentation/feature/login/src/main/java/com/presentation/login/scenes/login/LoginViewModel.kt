package com.presentation.login.scenes.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.UpAuthUseCase
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import token_storage.TokenStoreService
import javax.inject.Inject

data class LoginUIState(
    val accessToken: String = "",
    val shoudShowCreateAccountDialog: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val upAuthUseCase: UpAuthUseCase
) : ViewModel() {
    enum class Action {
        SuccessKakaoLogin,
        FailedKakaoLogin,
        DidTapCloseButton,
        CompletedSignUp
    }

    private var _uiState = MutableStateFlow(value = LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.SuccessKakaoLogin -> {
                val oAuthToken = value as? OAuthToken ?: return
                viewModelScope.launch {
                    try {
                        val result = upAuthUseCase.login(oAuthToken = oAuthToken.accessToken)
                        TokenStoreService.save(loginResult = result)
                        // TODO: Navigate To Main
                        val savedAccess = TokenStoreService.accessToken()
                        val savedRefresh = TokenStoreService.refreshToken()
                        val savedIsAvailable = TokenStoreService.isAccessTokenValid()

                        Log.d("값들", "${savedAccess} + ${savedRefresh} + ${savedIsAvailable} ")
                    } catch (error: HttpException) {
                        when (error.code()) {
                            404 -> _uiState.update { it.copy(accessToken = oAuthToken.accessToken, shoudShowCreateAccountDialog = true) }
                        }
                    }
                }
            }

            Action.FailedKakaoLogin -> {
                Log.e(TAG, "카카오 로그인 실패: $value")
            }

            Action.DidTapCloseButton -> {
                _uiState.update { it.copy(shoudShowCreateAccountDialog = false, accessToken = "") }
            }
            Action.CompletedSignUp -> {
                _uiState.update { it.copy(shoudShowCreateAccountDialog = false) }
                viewModelScope.launch {
                    val result = upAuthUseCase.login(oAuthToken = currentState.accessToken)
                    TokenStoreService.save(loginResult = result)
                    // TODO: Navigate To Main
                }
            }
        }
    }
}