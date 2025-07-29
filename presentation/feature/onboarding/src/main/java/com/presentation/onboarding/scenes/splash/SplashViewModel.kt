package com.presentation.onboarding.scenes.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.LoginResult
import com.domain.usecase.UpAuthUseCase
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.navigation_constant.NavigationRouteConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import token_storage.TokenStoreService
import javax.inject.Inject

sealed interface SplashUIEvent {
    data object NavigateTo : SplashUIEvent
}

data class SplashUIState(
    val destination: String = NavigationRouteConstant.loginNestedRoute
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val upAuthUseCase: UpAuthUseCase
): ViewModel() {
    enum class Action {
        OnAppear
    }

    private val _uiState = MutableStateFlow(SplashUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<SplashUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.OnAppear -> {
                viewModelScope.launch {
                    try {
                        // 1. 토큰 재 발급을 시킴 (저장) --> NavigateTo Main
                        val refreshToken = TokenStoreService.refreshToken()
                        val (newAccess, newRefresh) = upAuthUseCase.reissueToken(refreshToken = refreshToken)
                        TokenStoreService.save(LoginResult(accessToken = newAccess, refreshToken = newRefresh, expiresIn = 0))
                        _uiState.update { it.copy(destination = NavigationRouteConstant.mainNestedRoute) }
                    } catch (error: APIException) {
                        // 2. 실패 시, 온보딩 노출 여부에 따라 온보딩 or 로그인으로 보낸다.
                        if (UpDataStoreService.needOnBoardingScene) {
                            _uiState.update { it.copy(destination = NavigationRouteConstant.onboardingSceneRoute) }
                        } else {
                            Log.d("그래서 여기타는거아닌가?", "ㅇㅇ")
                            _uiState.update { it.copy(destination = NavigationRouteConstant.loginNestedRoute) }
                        }
                    } finally {
                        // 스플래시를 최소 2초간 보여주기 위한 지연
                        delay(2000)
                        _event.emit(SplashUIEvent.NavigateTo)
                    }
                }
            }
        }
    }
}