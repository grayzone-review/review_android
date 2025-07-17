package com.presentation.login.scenes.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.location.UpLocationService
import com.data.storage.datastore.UpDataStoreService
import com.domain.usecase.UpAuthUseCase
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import token_storage.TokenStoreService
import javax.inject.Inject

sealed interface LoginUIEvent {
    data object ShowSettingAlert : LoginUIEvent
}

data class LoginUIState(
    val accessToken: String = "",
    val shouldShowCreateAccountDialog: Boolean = false,
    val shouldShowSettingAlert: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val upAuthUseCase: UpAuthUseCase
) : ViewModel() {
    enum class Action {
        SuccessKakaoLogin,
        FailedKakaoLogin,
        DidTapCloseButton,
        CompletedSignUp,
        CacheLocation,
        ShowSettingAlert,
        DismissSettingAlert
    }

    private var _uiState = MutableStateFlow(value = LoginUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<LoginUIEvent>()
    val event = _event.asSharedFlow()

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
                            404 -> _uiState.update { it.copy(accessToken = oAuthToken.accessToken, shouldShowCreateAccountDialog = true) }
                        }
                    }
                }
            }

            Action.FailedKakaoLogin -> {
                Log.e(TAG, "카카오 로그인 실패: $value")
            }

            Action.DidTapCloseButton -> {
                _uiState.update { it.copy(shouldShowCreateAccountDialog = false, accessToken = "") }
            }
            Action.CompletedSignUp -> {
                _uiState.update { it.copy(shouldShowCreateAccountDialog = false) }
                viewModelScope.launch {
                    val result = upAuthUseCase.login(oAuthToken = currentState.accessToken)
                    TokenStoreService.save(loginResult = result)
                    // TODO: Navigate To Main
                }
            }
            Action.CacheLocation -> {
                viewModelScope.launch {
                    val (lat, long) = UpLocationService.fetchCurrentLocation() ?: return@launch
                    UpDataStoreService.lastKnownLocation = "${lat},${long}"
                    Log.d("로그인-캐시저장", "${lat} / ${long}")
                }
            }
            Action.ShowSettingAlert -> {
                _uiState.update { it.copy(shouldShowSettingAlert = true) }
            }
            Action.DismissSettingAlert -> {
                _uiState.update { it.copy(shouldShowSettingAlert = false) }
            }

        }
    }
}