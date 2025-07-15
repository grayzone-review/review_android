package com.presentation.login.scenes.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Agreement
import com.domain.entity.LegalDistrictInfo
import com.domain.entity.TermInfo
import com.domain.usecase.UpAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import edit_profile_address_component.FieldState
import edit_profile_address_component.NickNameField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TermCheck(
    val info: TermInfo,
    val isChecked: Boolean = false
)

data class SignUpUIState(
    val nickNameField: NickNameField = NickNameField(),
    val myTown: LegalDistrictInfo? = null,
    val interestTowns: List<LegalDistrictInfo> = emptyList(),
    val terms: List<TermCheck> = emptyList(),
    val isSubmitEnabled: Boolean = false,
    val accessToken: String = ""
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val upAuthUseCase: UpAuthUseCase
) : ViewModel() {
    enum class Action {
        GetTerms,
        AddInterestTown,
        SetMyTown,
        UpdateNickNameTextField,
        DidTapCheckDuplicateButton,
        DidTapRemoveInterestTownButton,
        DidTapCheckBox,
        DidTapDetailButton,
        DidTapSubmitButton,
        SetAccessToken
    }

    private var _uiState = MutableStateFlow(value = SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.SetAccessToken -> {
                val token = value as? String ?: return
                _uiState.update {
                    it.copy(accessToken = token)
                }
            }
            Action.GetTerms -> {
                viewModelScope.launch {
                    val result = upAuthUseCase.terms()
                    val termChecks = result.terms.map { TermCheck(it, false) }
                    _uiState.update {
                        it.copy(terms = termChecks)
                            .checkSubmitValidation()
                    }
                }
            }
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
                _uiState.update {
                    it.copy(myTown = selectedDistrictInfo)
                        .checkSubmitValidation()
                }
            }
            Action.UpdateNickNameTextField -> {
                val newNickname = value as? String ?: return
                val (fieldState, errorMessage) = if (newNickname.isNotEmpty() && newNickname.length < 2)
                    FieldState.ClientError to "※ 2~12자 이내로 입력가능하며, 한글, 영문, 숫자 사용이 가능합니다."
                else
                    FieldState.Normal to ""
                val updatedField = _uiState.value.nickNameField.copy(
                    value = newNickname,
                    fieldState = fieldState,
                    errorMessage = errorMessage
                )
                _uiState.update {
                    it.copy(nickNameField = updatedField)
                        .checkSubmitValidation()
                }
            }
            Action.DidTapCheckDuplicateButton -> {
                val currentState = _uiState.value
                if (currentState.nickNameField.value.length < 2) return
                viewModelScope.launch {
                    val result = upAuthUseCase.verifyNickName(nickname = currentState.nickNameField.value)
                    val newNickNameField = NickNameField(
                        value = currentState.nickNameField.value,
                        fieldState = if (result.success) FieldState.ServerSuccess else FieldState.ClientError,
                        errorMessage = "※ ${result.message}"
                    )
                    _uiState.update {
                        it.copy(nickNameField = newNickNameField)
                            .checkSubmitValidation()
                    }
                }
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
                val code = value as? TermCode ?: return      // ALL | SERVICE | PRIVACY | LOCATION

                _uiState.update { state ->

                    /* 현재 약관 리스트 */
                    val updated = when (code) {
                        TermCode.ALL -> {
                            val toggle = !state.terms
                                .filter { it.info.required }
                                .all   { it.isChecked }
                            state.terms.map { term ->
                                if (term.info.required) term.copy(isChecked = toggle) else term
                            }
                        }
                        else -> state.terms.map { term ->
                            val termCode = when (term.info.code) {
                                "serviceUse" -> TermCode.SERVICE
                                "privacy"    -> TermCode.PRIVACY
                                "location"   -> TermCode.LOCATION
                                else         -> null
                            }
                            if (termCode == code) term.copy(isChecked = !term.isChecked) else term
                        }
                    }

                    state.copy(terms = updated)
                        .checkSubmitValidation()
                }
            }
            Action.DidTapDetailButton -> {
                // TODO: WebView Navigate
            }
            Action.DidTapSubmitButton -> {
                val currentState = _uiState.value
                if (currentState.myTown == null) return
                val agreements = currentState.terms
                    .filter { it.isChecked }
                    .mapNotNull { term ->
                        when (term.info.code) {
                            "serviceUse" -> Agreement.serviceUse
                            "privacy" -> Agreement.privacy
                            "location" -> Agreement.location
                            else -> null
                        }
                    }
                viewModelScope.launch {
                    val result = upAuthUseCase.signUp(
                        oauthToken = currentState.accessToken,
                        mainRegionId = currentState.myTown.id,
                        interestedRegionIds = currentState.interestTowns.map { it.id },
                        nickname = currentState.nickNameField.value,
                        agreements = agreements
                    )
                }
            }
        }
    }
}

private fun SignUpUIState.checkSubmitValidation(): SignUpUIState = copy(
    isSubmitEnabled = nickNameField.fieldState == FieldState.ServerSuccess &&
            myTown != null &&
            terms.filter { it.info.required }.all { it.isChecked }
)