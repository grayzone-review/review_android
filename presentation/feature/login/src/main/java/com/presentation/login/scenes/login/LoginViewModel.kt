package com.presentation.login.scenes.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.location.UpLocationService
import com.data.storage.datastore.UpDataStoreService
import com.domain.usecase.UpAuthUseCase
import com.kakao.sdk.auth.model.OAuthToken
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.ErrorAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import token_storage.TokenStoreService
import javax.inject.Inject

sealed interface LoginUIEvent {
//    data object ShowSettingAlert : LoginUIEvent
    data object NavigateToMain: LoginUIEvent
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
                Log.d("오스토큰:", value.toString())
                viewModelScope.launch {
                    try {
                        val result = upAuthUseCase.login(oAuthToken = oAuthToken.accessToken)
                        result?.let { bindingResult -> TokenStoreService.save(loginResult = bindingResult) }
                        _event.emit(LoginUIEvent.NavigateToMain)
                    } catch (error: APIException) {
                        Log.d("값들실패들", oAuthToken.accessToken)
                        if (error.action == ErrorAction.Login) {
                            _uiState.update { it.copy(shouldShowCreateAccountDialog = true, accessToken = oAuthToken.accessToken) }
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
                    try {
                        val result = upAuthUseCase.login(oAuthToken = currentState.accessToken)
                        Log.d("토큰값", result.toString())
                        result?.let { bindingResult -> TokenStoreService.save(loginResult = bindingResult) }
                        _event.emit(LoginUIEvent.NavigateToMain)
                    } catch (error: APIException) {
                        if (error.action == ErrorAction.Login) {
                            // TODO:
                        }
                    }
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