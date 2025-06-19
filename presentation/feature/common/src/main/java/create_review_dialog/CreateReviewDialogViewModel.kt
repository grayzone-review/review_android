package create_review_dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Company
import com.domain.entity.Ratings
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
    val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
    val phase: CreateReviewPhase = CreateReviewPhase.First,
    val company: Company? = null,
    val jobRole: String = "",
    val employmentPeriod: WorkPeriod? = null,
    val rating: Ratings? = null,
    val advantagePoint: String = "",
    val disadvantagePoint: String = "",
    val managementFeedBack: String = ""
) {

}

@HiltViewModel
class CreateReviewDialogViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        UpdateJobRole,
        DidTapNextButton,
        DidTapPreviousButton,
        DidTapSubmitButton,
        DidTapTextField,
        SheetDismissed,
        UpdateEmploymentPeriod
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
        }
    }
}