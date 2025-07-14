package create_review_dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Company
import com.domain.entity.Ratings
import com.domain.entity.SearchedCompany
import com.domain.usecase.SearchCompaniesUseCase
import create_review_dialog.contents.isFullyRated
import create_review_dialog.sheet_contents.WorkPeriod
import create_review_dialog.type.CreateReviewPhase
import create_review_dialog.type.InputField
import create_review_dialog.type.next
import create_review_dialog.type.prev
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class BottomSheetState {
    data object Hidden: BottomSheetState()
    data class Visible(val field: InputField): BottomSheetState()
}

data class CreateReviewUIState(
    // 회사 검색 시트 액션
    val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
    val searchTextFieldValue: String = "",
    val searchedCompanies: List<SearchedCompany> = emptyList(),
    // 페이지 관리
    val phase: CreateReviewPhase = CreateReviewPhase.First,
    val isNextAndSubmitEnabled: Boolean = false,
    // 사용자 입력 값
    val company: SearchedCompany? = null,
    val jobRole: String = "",
    val employmentPeriod: WorkPeriod? = null,
    val rating: Ratings = Ratings(),
    val advantagePoint: String = "",
    val disadvantagePoint: String = "",
    val managementFeedBack: String = ""
)

@HiltViewModel
class CreateReviewDialogViewModel @Inject constructor(
    private val searchCompaniesUseCase: SearchCompaniesUseCase
) : ViewModel() {
    enum class Action {
        // 모달, 시트 흐름 제어
        DidTapNextButton,
        DidTapPreviousButton,
        DidTapSubmitButton,
        DidTapTextField,
        SheetDismissed,
        // 사용자 입력 액션
        UpdateEmploymentPeriod,
        UpdateTextFieldValue,
        UpdateRatings,
        UpdateCompany,
        // 회사 검색 시트 액션
        UpdateSearchQuery,
        DidTapClearButton
    }

    private var _uiState = MutableStateFlow(value = CreateReviewUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.DidTapNextButton -> {
                _uiState.update { state ->
                    state.phase.next()?.let { nextPhase ->
                        val enabled = state.isPhaseCompleted(nextPhase)
                        state.copy(phase = nextPhase, isNextAndSubmitEnabled = enabled)
                    } ?: state
                }
            }
            Action.DidTapPreviousButton -> {
                _uiState.update { state ->
                    state.phase.prev()?.let { prevPhase ->
                        val enabled = state.isPhaseCompleted(prevPhase)
                        state.copy(phase = prevPhase, isNextAndSubmitEnabled = enabled)
                    } ?: state
                }
            }
            Action.DidTapSubmitButton -> {

            }
            Action.DidTapTextField -> {
                val field = value as? InputField ?: return
                _uiState.update { state ->
                    state.copy(bottomSheetState = BottomSheetState.Visible(field))
                }
            }
            Action.SheetDismissed -> {
                _uiState.update { state ->
                    state.copy(bottomSheetState = BottomSheetState.Hidden)
                }
            }
            Action.UpdateEmploymentPeriod -> {
                val period = value as? WorkPeriod ?: return
                _uiState.update { state ->
                    val updatedState = state.copy(
                        employmentPeriod = period
                    )
                    updatedState.copy(
                        isNextAndSubmitEnabled = updatedState.isFirstPhaseCompleted()
                    )
                }
            }
            Action.UpdateTextFieldValue -> {
                val (field, text) = value as? Pair<InputField,String> ?: return
                _uiState.update { state ->
                    val updated = when (field) {
                        InputField.JobRole -> state.copy(jobRole = text)
                        InputField.Advantage -> state.copy(advantagePoint = text)
                        InputField.Disadvantage -> state.copy(disadvantagePoint = text)
                        InputField.ManagementFeedback -> state.copy(managementFeedBack = text)
                        else -> state
                    }
                    val enabled = when (updated.phase) {
                        CreateReviewPhase.First -> updated.isFirstPhaseCompleted()
                        CreateReviewPhase.Second -> updated.isSecondPhaseCompleted()
                        CreateReviewPhase.Third -> updated.isThirdPhaseCompleted()
                    }
                    updated.copy(isNextAndSubmitEnabled = enabled)
                }
            }
            Action.UpdateRatings -> {
                val newRatings = value as? Ratings ?: return
                _uiState.update { state ->
                    val updatedState = state.copy(
                        rating = newRatings
                    )
                    updatedState.copy(
                        isNextAndSubmitEnabled = updatedState.isSecondPhaseCompleted()
                    )
                }
            }
            Action.UpdateSearchQuery -> {
                val query = value as? String ?: return
                _uiState.update {
                    it.copy(searchTextFieldValue = query)
                }
                searchCompanies(query = query)
            }
            Action.UpdateCompany -> {
                val newCompany = value as? SearchedCompany ?: return
                _uiState.update { state ->
                    val stateWithCompany = state.copy(
                        company = newCompany
                    )
                    stateWithCompany.copy(
                        isNextAndSubmitEnabled = stateWithCompany.isFirstPhaseCompleted()
                    )
                }
            }
            Action.DidTapClearButton -> {
                _uiState.update { state ->
                    state.copy(
                        searchTextFieldValue = "",
                        searchedCompanies = emptyList()
                    )
                }
            }
        }
    }

    private fun searchCompanies(query: String) {
        if (query.isBlank()) {
            clearSearchResults()
            return
        }

        viewModelScope.launch {
            try {
                val result = searchCompaniesUseCase.searchCompanies(
                    keyword = query,
                    latitude = 37.5665,
                    longitude = 126.9780,
                    size = 20,
                    page = 0
                )

                _uiState.update { it.copy(searchedCompanies = result.companies) }
            } catch (e: Exception) {

            } finally {
                // 로딩 처리
            }
        }
    }

    private fun clearSearchResults() {
        _uiState.update { it.copy(searchedCompanies = emptyList()) }
    }

    private fun CreateReviewUIState.isFirstPhaseCompleted(): Boolean =
        company != null && jobRole.isNotBlank() && employmentPeriod != null

    private fun CreateReviewUIState.isSecondPhaseCompleted(): Boolean =
        rating.isFullyRated()

    private fun CreateReviewUIState.isThirdPhaseCompleted(): Boolean =
        advantagePoint.isNotBlank() && disadvantagePoint.isNotBlank() && managementFeedBack.isNotBlank()

    private fun CreateReviewUIState.isPhaseCompleted(
        phase: CreateReviewPhase = this.phase
    ): Boolean = when (phase) {
        CreateReviewPhase.First  -> isFirstPhaseCompleted()
        CreateReviewPhase.Second -> isSecondPhaseCompleted()
        CreateReviewPhase.Third  -> isThirdPhaseCompleted()
    }
}