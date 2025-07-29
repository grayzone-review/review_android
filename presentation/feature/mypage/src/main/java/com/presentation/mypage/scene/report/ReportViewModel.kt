package com.presentation.mypage.scene.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.User
import com.domain.usecase.UserUseCase
import com.presentation.mypage.scene.report.type.ReportReason
import com.team.common.feature_api.error.APIException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ReportUIEvent {
    data class ShowAlert(val error: APIException? = null) : ReportUIEvent
    data class ShowSuccessAlert(val message: String): ReportUIEvent
}

data class ReportUIState(
    val user: User = User(),
    val reason: ReportReason? = null,
    val reportedUserNickname: String = "",
    val detailReason: String = "",
    val isSubmitEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        UpdateSelectReason,
        UpdateReportedUserNickName,
        UpdateDetailReason,
        Submit
    }

    private var _uiState = MutableStateFlow(value = ReportUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<ReportUIEvent>()
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
            Action.UpdateSelectReason -> {
                val selectedReason = value as? ReportReason ?: return
                _uiState.update {
                    it.copy(reason = selectedReason)
                        .checkSubmitValidation()
                }
            }
            Action.UpdateReportedUserNickName -> {
                val reportedUserNickname = value as? String ?: return
                _uiState.update {
                    it.copy(reportedUserNickname = reportedUserNickname)
                        .checkSubmitValidation()
                }
            }
            Action.UpdateDetailReason -> {
                val detailReason = value as? String ?: return
                _uiState.update {
                    it.copy(detailReason = detailReason)
                        .checkSubmitValidation()
                }
            }
            Action.Submit -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    try {
                        userUseCase.report(
                            reporterName = currentState.user.nickname ?: "",
                            targetName = currentState.reportedUserNickname,
                            reportType = currentState.reason?.rawValue ?: "",
                            description = currentState.detailReason
                        )
                        _event.emit(ReportUIEvent.ShowSuccessAlert("신고가 접수되었습니다. 검토 후 조치하겠습니다."))
                        _uiState.update { it.copy(isLoading = false) }
                    } catch (error: APIException) {
                        _event.emit(ReportUIEvent.ShowAlert(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
}
private fun ReportUIState.checkSubmitValidation():ReportUIState = copy(
    isSubmitEnabled = reason!=null &&
            ((reason==ReportReason.BUG) || reportedUserNickname.isNotEmpty()) &&
            detailReason.isNotEmpty()
)