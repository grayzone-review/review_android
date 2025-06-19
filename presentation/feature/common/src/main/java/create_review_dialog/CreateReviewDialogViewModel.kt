package create_review_dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.domain.entity.Company
import com.domain.entity.Ratings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class CreateReviewPhase {
    First,
    Second,
    Third
}

val CreateReviewPhase.step: Int
    get() = when (this) {
        CreateReviewPhase.First -> 1
        CreateReviewPhase.Second -> 2
        CreateReviewPhase.Third -> 3
    }

fun CreateReviewPhase.next(): CreateReviewPhase? = when (this) {
    CreateReviewPhase.First  -> CreateReviewPhase.Second
    CreateReviewPhase.Second -> CreateReviewPhase.Third
    CreateReviewPhase.Third  -> null
}

fun CreateReviewPhase.prev(): CreateReviewPhase? = when (this) {
    CreateReviewPhase.First  -> null
    CreateReviewPhase.Second -> CreateReviewPhase.First
    CreateReviewPhase.Third  -> CreateReviewPhase.Second
}

data class CreateReviewUIState(
    val phase: CreateReviewPhase = CreateReviewPhase.First,
    val company: Company? = null,
    val jobRole: String = "",
    val employmentPeriod: String = "",
    val rating: Ratings? = null,
    val advantagePoint: String = "",
    val disadvantagePoint: String = "",
    val managementFeedBack: String = ""
)

@HiltViewModel
class CreateReviewDialogViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        UpdateJobRole,
        DidTapNextButton,
        DidTapPreviousButton,
        DidTapSubmitButton
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
        }
    }
}