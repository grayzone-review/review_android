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
    // 바텀 시트
    val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
    val searchTextFieldValue: String = "",
    val searchedCompanies: List<SearchedCompany> = emptyList(),
    // 페이지 관리
    val phase: CreateReviewPhase = CreateReviewPhase.First,
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
        UpdateJobRole,
        DidTapNextButton,
        DidTapPreviousButton,
        DidTapSubmitButton,
        DidTapTextField,
        SheetDismissed,
        UpdateEmploymentPeriod,
        UpdateTextFieldValue,
        UpdateRatings,
        UpdateCompany,
        UpdateSearchQuery,
        DidTapClearButton
    }

    private var _uiState = MutableStateFlow(value = CreateReviewUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.UpdateJobRole -> {
                val newJobRole = value as? String ?: return
                _uiState.update { it.copy(jobRole = newJobRole) }
            }
            Action.DidTapNextButton -> {
                _uiState.update { state ->
                    state.phase.next()?.let { next -> state.copy(phase = next) } ?: state
                }
            }
            Action.DidTapPreviousButton -> {
                _uiState.update { state ->
                    state.phase.prev()?.let { prv -> state.copy(phase = prv) } ?: state
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
                val field = value as? WorkPeriod ?: return
                viewModelScope.launch {
                    _uiState.update { state ->
                        state.copy(employmentPeriod = field)
                    }
                }
            }
            Action.UpdateTextFieldValue -> {
                val (field, text) = value as? Pair<InputField,String> ?: return
                _uiState.update { state ->
                    when (field) {
                        InputField.JobRole -> state.copy(jobRole = text)
                        InputField.Advantage -> state.copy(advantagePoint = text)
                        InputField.Disadvantage -> state.copy(disadvantagePoint = text)
                        InputField.ManagementFeedback -> state.copy(managementFeedBack = text)
                        else -> state
                    }
                }
            }
            Action.UpdateRatings -> {
                val newRatings = value as? Ratings ?: return
                _uiState.update { state ->
                    state.copy(rating = newRatings)
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
                    state.copy(company = newCompany)
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

}