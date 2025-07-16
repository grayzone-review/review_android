package com.presentation.mypage.scene.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.LegalDistrictInfo
import com.domain.entity.User
import com.presentation.mypage.scene.report.type.ReportReason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportUIState(
    val user: User = User(),
    val reason: ReportReason? = null,
    val reportedUserNickname: String = "",
    val detailReason: String = "",
    val isSubmitEnabled: Boolean = false
)

@HiltViewModel
class ReportViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {
        GetUser,
        UpdateSelectReason,
        UpdateReportedUserNickName,
        UpdateDetailReason,
        Submit
    }

    private var _uiState = MutableStateFlow(value = ReportUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.GetUser -> {
                viewModelScope.launch {
                    val user = getMockUser()
                    _uiState.update { it.copy(user = user) }
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
                /* TODO: Submit API
                *   제출 후, 성공하면 event로 PopTo 를 방출한다. */
            }
        }
    }

    private fun getMockUser(): User {
        return User(
            nickname = "서현웅",
            mainRegion = LegalDistrictInfo(1, "서울시 중랑구 면목동"),
            interestedRegions = listOf(
                LegalDistrictInfo(1001, "서울시 중랑구 면목동"),
                LegalDistrictInfo(2002, "서울시 중랑구 중곡동"),
                LegalDistrictInfo(3003, "서울시 중랑구 상봉동")
            )
        )
    }
}

private fun ReportUIState.checkSubmitValidation(): ReportUIState = copy(
    isSubmitEnabled = reason != null && reportedUserNickname.isNotEmpty() && detailReason.isNotEmpty()
)