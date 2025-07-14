package com.presentation.mypage.scene

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed interface MyPageUIEvent {
//    data object ShowSettingAlert : MyPageUIEvent
}

data class MyPageUIState(
    val a: String = ""
)

@HiltViewModel
class MyPageViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {
    }

    private var _uiState = MutableStateFlow(value = MyPageUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MyPageUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {

    }

}