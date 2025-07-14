package com.presentation.mypage.scene

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class MyPageSection { ACCOUNT, SUPPORT, ACTIVITY, ETC }

enum class MyPageMenu(
    val title:String,
    val section:MyPageSection
) {
    UPDATE_PROFILE("내 정보 수정", MyPageSection.ACCOUNT),
    REPORT("신고하기", MyPageSection.ACCOUNT),
    REVIEW_HISTORY("리뷰 작성 내역", MyPageSection.ACTIVITY),
    WITHDRAW("회원 탈퇴", MyPageSection.ETC),
    LOGOUT("로그아웃", MyPageSection.ETC)
}

sealed interface MyPageUIEvent {
    data class NavigateTo(val menu: MyPageMenu): MyPageUIEvent
    data class ShowAlert(val menu: MyPageMenu): MyPageUIEvent
}

data class MyPageUIState(
    val user: User = User()
)

@HiltViewModel
class MyPageViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {
        GetUser,
        DidTapMyPageMenu
    }

    private var _uiState = MutableStateFlow(value = MyPageUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MyPageUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.GetUser -> {
                viewModelScope.launch {
                    val user = getMockUser()
                    _uiState.update { it.copy(user = user) }
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
        }
    }

    private fun getMockUser(): User {
        return User(
            nickname = "서현웅",
            mainRegion = "서울시 중랑구 면목동",
            interestedRegions = listOf(
                "서울시 중랑구 면목동",
                "서울시 중랑구 중곡동",
                "서울시 중랑구 상봉동"
            )
        )
    }
}