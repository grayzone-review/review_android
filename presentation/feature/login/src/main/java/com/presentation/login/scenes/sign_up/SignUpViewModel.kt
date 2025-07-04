package com.presentation.login.scenes.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.LegalDistrictInfo
import com.domain.usecase.UpAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    val myTown: LegalDistrictInfo? = null,
    val interestTowns: List<LegalDistrictInfo> = emptyList(),
    val terms: TermsAgreement = TermsAgreement(),
    val isSubmitEnabled: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val upAuthUseCase: UpAuthUseCase
) : ViewModel() {
    enum class Action {
        AddInterestTown,
        SetMyTown,
        UpdateNickNameTextField,
        DidTapCheckDuplicateButton,
        DidTapRemoveInterestTownButton,
        DidTapCheckBox,
        DidTapDetailButton,
        DidTapSubmitButton
    }

    private var _uiState = MutableStateFlow(value = SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.AddInterestTown -> {
                val currentState = _uiState.value
                val addedLegalDistrictInfo = value as? LegalDistrictInfo ?: return
                if (currentState.interestTowns.any { it.id == addedLegalDistrictInfo.id }) return
                _uiState.update {
                    it.copy(interestTowns = currentState.interestTowns + addedLegalDistrictInfo)
                }
            }
            Action.SetMyTown -> {
                val selectedDistrictInfo = value as? LegalDistrictInfo ?: return
                _uiState.update { it.copy(myTown = selectedDistrictInfo) }
            }
            Action.UpdateNickNameTextField -> {
                val newNickname = value as? String ?: return
                _uiState.update {
                    it.copy(nickname = newNickname)
                        .checkSubmitValidation()
                }
            }
            Action.DidTapCheckDuplicateButton -> {
                val currentState = _uiState.value
                viewModelScope.launch {
                    val result = upAuthUseCase.verifyNickName(nickname = currentState.nickname)
                    Log.d("리저트:", result.toString())
                }
            }
            Action.DidTapSubmitButton -> {
                // TODO: Check Submit -> API 나오면 처리
            }
            Action.DidTapRemoveInterestTownButton -> {
                val targetDistrictInfo = value as? LegalDistrictInfo ?: return
                _uiState.update { state ->
                    state.copy(interestTowns = state.interestTowns
                        .filterNot { it.id == targetDistrictInfo.id }
                    )
                }
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
//    isSubmitEnabled = nickname.isNotBlank() && myTown?.isNotBlank() && terms.all
)
