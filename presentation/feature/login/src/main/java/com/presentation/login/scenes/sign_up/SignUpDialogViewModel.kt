package com.presentation.login.scenes.sign_up

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TermsAgreement(
    val service: Boolean = false,   // [필수] 서비스 이용 약관
    val privacy: Boolean = false,   // [필수] 개인정보 수집‧이용
    val location: Boolean = false   // [필수] 위치 기반 서비스
) {
    val all: Boolean
        get() = service && privacy && location
}

data class SignUpUIState(
    val nickname: String = "",
    val myTown: String = "",
    val interestTowns: List<String> = emptyList(),
    val terms: TermsAgreement = TermsAgreement()
)

class SignUpDialogViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        Hi

    }

    private var _uiState = MutableStateFlow(value = SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) = when (action) {
        Action.Hi -> {

        }
    }

}