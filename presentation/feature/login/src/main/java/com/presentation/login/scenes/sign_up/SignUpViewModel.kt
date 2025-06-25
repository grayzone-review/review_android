package com.presentation.login.scenes.sign_up

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class TermKind { ALL, SERVICE, PRIVACY, LOCATION }

data class TermsAgreement(
    val service: Boolean = false,
    val privacy: Boolean = false,
    val location: Boolean = false
) {
    val all: Boolean
        get() = service && privacy && location
}

data class SignUpUIState(
    val nickname: String = "",
    val myTown: String = "",
    val interestTowns: List<String> = listOf("수유3동", "면목동"),
    val terms: TermsAgreement = TermsAgreement(),
    val isSubmitEnabled: Boolean = false
)

class SignUpViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        UpdateNickNameTextField,
        DidTapCheckDuplicateButton,
        DidTapMyTownTextFieldButton,
        DidTapRemoveInterestTownButton,
        DidTapAddInterestTownButton,
        DidTapCheckBox,
        DidTapDetailButton,
        DidTapSubmitButton
    }

    private var _uiState = MutableStateFlow(value = SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.UpdateNickNameTextField -> {
                val newNickname = value as? String ?: return
                _uiState.update {
                    it.copy(nickname = newNickname)
                        .checkSubmitValidation()
                }
            }
            Action.DidTapCheckDuplicateButton -> {
                // TODO: Check 듀플리케이트 -> API 나오면 처리
            }
            Action.DidTapSubmitButton -> {
                // TODO: Check Submit -> API 나오면 처리
            }
            Action.DidTapMyTownTextFieldButton -> {
                // TODO: MyTownTextFieldButton Navigate
            }
            Action.DidTapRemoveInterestTownButton -> {
                val targetInterest = value as? String ?: return
                _uiState.update { state ->
                    state.copy(interestTowns = state.interestTowns
                        .filterNot { it == targetInterest }
                    )
                }
            }
            Action.DidTapAddInterestTownButton -> {
                // TODO: MyTownTextFieldButton Navigate
            }
            Action.DidTapCheckBox -> {
                val kind = value as? TermKind ?: return
                _uiState.update { state ->
                    val current = state.terms
                    val newTerms = when (kind) {
                        TermKind.ALL -> current.copy(service = !current.all, privacy = !current.all, location = !current.all)
                        TermKind.SERVICE -> current.copy(service = !current.service)
                        TermKind.PRIVACY -> current.copy(privacy = !current.privacy)
                        TermKind.LOCATION -> current.copy(location = !current.location)
                    }
                    state.copy(terms = newTerms)
                        .checkSubmitValidation()
                }
            }
            Action.DidTapDetailButton -> {
                // TODO: WebView Navigate
            }
        }
    }

}

private fun SignUpUIState.checkSubmitValidation(): SignUpUIState = copy(
    isSubmitEnabled = nickname.isNotBlank() && myTown.isNotBlank() && terms.all
)
