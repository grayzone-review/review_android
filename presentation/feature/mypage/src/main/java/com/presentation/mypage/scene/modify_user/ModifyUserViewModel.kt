package com.presentation.mypage.scene.modify_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.LegalDistrictInfo
import com.domain.usecase.UpAuthUseCase
import com.domain.usecase.UserUseCase
import com.team.common.feature_api.error.APIException
import dagger.hilt.android.lifecycle.HiltViewModel
import edit_profile_address_component.FieldState
import edit_profile_address_component.NickNameField
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ModifyUserUIEvent {
    data class ShowAlert(val error: APIException? = null) : ModifyUserUIEvent
    data class ShowSuccessAlert(val message: String): ModifyUserUIEvent
}

data class ModifyUserUIState(
    val nickNameField: NickNameField = NickNameField(),
    val myTown: LegalDistrictInfo? = null,
    val interestTowns: List<LegalDistrictInfo> = emptyList(),
    val isSubmitEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class ModifyUserViewModel @Inject constructor(
    private val upAuthUseCase: UpAuthUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        AddInterestTown,
        SetMyTown,
        UpdateNickNameTextField,
        DidTapCheckDuplicateButton,
        DidTapRemoveInterestTownButton,
        DidTapSubmitButton
    }

    private var _uiState = MutableStateFlow(value = ModifyUserUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<ModifyUserUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.OnAppear -> {
                if (currentState.nickNameField.value.isNotEmpty()) return
                viewModelScope.launch {
                    val result = userUseCase.userInfo()
                    val userNickNameField = NickNameField(
                        value = result.nickname ?: "",
                        fieldState = FieldState.ServerSuccess,
                        errorMessage = ""
                    )
                    _uiState.update {
                        it.copy(
                            nickNameField = userNickNameField,
                            myTown = result.mainRegion,
                            interestTowns = result.interestedRegions ?: emptyList(),
                            isSubmitEnabled = true
                        )
                    }
                }
            }
            Action.AddInterestTown -> {
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
                val updatedField = currentState.nickNameField.copy(
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
            Action.DidTapSubmitButton -> {
                viewModelScope.launch {
                    try {
                        _uiState.update { it.copy(isLoading = true) }
                        userUseCase.modifyUserInfo(
                            mainRegionID = currentState.myTown?.id ?: 0,
                            interestedRegionIds = currentState.interestTowns.map { it.id },
                            nickname = currentState.nickNameField.value
                        )
                        _event.emit(ModifyUserUIEvent.ShowSuccessAlert("내 정보가 성공적으로 수정되었습니다."))
                        _uiState.update { it.copy(isLoading = false) }
                    } catch (error: APIException) {
                        _event.emit(ModifyUserUIEvent.ShowAlert(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
}

private fun ModifyUserUIState.checkSubmitValidation(): ModifyUserUIState = copy(
    isSubmitEnabled = nickNameField.fieldState == FieldState.ServerSuccess && myTown != null
)