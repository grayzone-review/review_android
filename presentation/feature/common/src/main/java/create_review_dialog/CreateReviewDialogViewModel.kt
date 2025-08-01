package create_review_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.location.UpLocationService
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.CompactCompany
import com.domain.entity.Ratings
import com.domain.usecase.ReviewUseCase
import com.domain.usecase.SearchCompaniesUseCase
import com.team.common.feature_api.error.APIException
import create_review_dialog.contents.isFullyRated
import create_review_dialog.sheet_contents.WorkPeriod
import create_review_dialog.type.CreateReviewPhase
import create_review_dialog.type.InputField
import create_review_dialog.type.next
import create_review_dialog.type.prev
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface CreateReviewUIEvent {
    data object DismissDialog : CreateReviewUIEvent
    data class ShowAlert(val error: APIException? = null) : CreateReviewUIEvent
    data class ShowSuccessAlert(val message: String): CreateReviewUIEvent
}

sealed class BottomSheetState {
    data object Hidden: BottomSheetState()
    data class Visible(val field: InputField): BottomSheetState()
}

data class CreateReviewUIState(
    // 회사 검색 시트 액션
    val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
    val searchTextFieldValue: String = "",
    val searchedCompanies: List<CompactCompany> = emptyList(),
    // 페이지 관리
    val phase: CreateReviewPhase = CreateReviewPhase.First,
    val isNextAndSubmitEnabled: Boolean = false,
    val isLoading: Boolean = false,
    // 사용자 입력 값
    val company: CompactCompany? = null,
    val jobRole: String = "",
    val employmentPeriod: WorkPeriod? = null,
    val rating: Ratings = Ratings(),
    val advantagePoint: String = "",
    val disadvantagePoint: String = "",
    val managementFeedBack: String = ""
)

@HiltViewModel
class CreateReviewDialogViewModel @Inject constructor(
    private val searchCompaniesUseCase: SearchCompaniesUseCase,
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        // 모달, 시트 흐름 제어
        OnAppear,
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
        DidTapClearButton,
        // 초기화
        Reset
    }

    private var _uiState = MutableStateFlow(value = CreateReviewUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CreateReviewUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.OnAppear -> {
                val company = value as? CompactCompany ?: return
                _uiState.update { it.copy(company = company) }
            }
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
                _uiState.update { it.copy(searchTextFieldValue = query)}
                viewModelScope.launch {
                    if (query.isBlank()) {
                        clearSearchResults()
                        return@launch
                    }

                    val (lat, lng) = runCatching {
                        UpDataStoreService.lastKnownLocation
                            .split(',')
                            .map { it.toDouble() }
                            .let { it[0] to it[1] }
                    }.getOrElse {
                        UpLocationService.DEFAULT_SEOUL_TOWNHALL
                    }

                    val result = searchCompaniesUseCase.searchCompanies(
                        keyword = query,
                        latitude = lat,
                        longitude = lng,
                        size = 20,
                        page = 0
                    )
                    _uiState.update { it.copy(searchedCompanies = result.companies) }
                }

            }
            Action.UpdateCompany -> {
                val newCompany = value as? CompactCompany ?: return
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
            Action.DidTapSubmitButton -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    try {
                        reviewUseCase.createReview(
                            companyID = currentState.company?.id ?: 0,
                            advantagePoint = currentState.advantagePoint,
                            disadvantagePoint = currentState.disadvantagePoint,
                            managementFeedback = currentState.managementFeedBack,
                            jobRole = currentState.jobRole,
                            employmentPeriod = currentState.employmentPeriod?.rawValue ?: "",
                            welfare = currentState.rating.welfare ?: 0.0,
                            workLifeBalance = currentState.rating.workLifeBalance ?: 0.0,
                            salary = currentState.rating.salary ?: 0.0,
                            companyCulture = currentState.rating.companyCulture ?: 0.0,
                            management = currentState.rating.management ?: 0.0
                        )
                        _event.emit(CreateReviewUIEvent.ShowSuccessAlert("리뷰가 정상적으로 등록되었습니다!"))
                        _uiState.update { it.copy(isLoading = false) }
                    } catch (error: APIException) {
                        _event.emit(CreateReviewUIEvent.ShowAlert(error))
                    }
                }
            }
            Action.Reset -> { _uiState.update { CreateReviewUIState() } }
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