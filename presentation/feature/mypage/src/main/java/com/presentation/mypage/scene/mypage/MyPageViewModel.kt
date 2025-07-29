package com.presentation.mypage.scene.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.User
import com.domain.usecase.UpAuthUseCase
import com.domain.usecase.UserUseCase
import com.team.common.feature_api.error.APIException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import token_storage.TokenStoreService
import javax.inject.Inject


enum class MyPageSection { ACCOUNT, SUPPORT, ACTIVITY, ETC }

enum class MyPageMenu(val title:String, val section: MyPageSection) {
    UPDATE_PROFILE("내 정보 수정", MyPageSection.ACCOUNT),
    REPORT("신고하기", MyPageSection.ACCOUNT),
    REVIEW_HISTORY("리뷰 작성 내역", MyPageSection.ACTIVITY),
    WITHDRAW("회원 탈퇴", MyPageSection.ETC),
    LOGOUT("로그아웃", MyPageSection.ETC)
}

sealed interface MyPageUIEvent {
    data class NavigateTo(val menu: MyPageMenu): MyPageUIEvent
    data class ShowAlert(val menu: MyPageMenu): MyPageUIEvent
    data class ShowErrorAlert(val error: APIException? = null) : MyPageUIEvent
    data class ShowSuccessAlert(val message: String): MyPageUIEvent
}

data class MyPageUIState(
    val user: User = User(),
    val shouldShowCreateReviewSheet: Boolean = false
)

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val upAuthUseCase: UpAuthUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        DidTapMyPageMenu,
        ShowCreateReviewSheet,
        ConfirmResign,
        ConfirmLogout,
        DismissCreateReviewSheet
    }

    private var _uiState = MutableStateFlow(value = MyPageUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MyPageUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.OnAppear -> {
                viewModelScope.launch {
                    val result = userUseCase.userInfo()
                    result?.let { bindingResult -> _uiState.update { it.copy(user = bindingResult) } }
                }
            }
            Action.DidTapMyPageMenu -> {
                val myPageMenu = value as? MyPageMenu ?: return
                viewModelScope.launch {
                    when (myPageMenu) {
                        MyPageMenu.WITHDRAW, MyPageMenu.LOGOUT -> {
                            _event.emit(MyPageUIEvent.ShowAlert(myPageMenu))
                        }
                        else -> {
                            _event.emit(MyPageUIEvent.NavigateTo(myPageMenu))
                        }
                    }
                }
            }
            Action.ShowCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = true) } }
            Action.DismissCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = false) } }
            Action.ConfirmResign -> {
                viewModelScope.launch {
                    try {
                        val refreshToken = TokenStoreService.refreshToken()
                        userUseCase.resign(refreshToken = refreshToken)
                        TokenStoreService.clear()
                        _event.emit(MyPageUIEvent.ShowSuccessAlert("회원탈퇴가 완료되었습니다."))
                    } catch (error: APIException) {
                        _event.emit(MyPageUIEvent.ShowErrorAlert(error))
                    }
                }
            }
            Action.ConfirmLogout -> {
                viewModelScope.launch {
                    try {
                        val refreshToken = TokenStoreService.refreshToken()
                        upAuthUseCase.logout(refreshToken = refreshToken)
                        TokenStoreService.clear()
                        _event.emit(MyPageUIEvent.ShowSuccessAlert("로그아웃이 완료되었습니다."))
                    } catch (error: APIException) {
                        _event.emit(MyPageUIEvent.ShowErrorAlert(error))
                    }
                }
            }
        }
    }
}