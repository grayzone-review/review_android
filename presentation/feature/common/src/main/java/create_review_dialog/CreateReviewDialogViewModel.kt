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
import javax.inject.Inject

enum class CreateReviewPhase {
    First,
    Second,
    Third
}

data class CreateReviewUIState(
    val phase: CreateReviewPhase = CreateReviewPhase.First,
    val companyName: Company? = null,
    val jobRole: String = "",
    val employmentPeriod: String = "",
    val rating: Ratings? = null,
    val advantagePoint: String = "",
    val disadvantagePoint: String = "",
    val managementFeedBack: String = "",
    val showDialog: Boolean = false
)

@HiltViewModel
class CreateReviewDialogViewModel @Inject constructor() : ViewModel() {
    enum class Action {

    }

    private var _uiState = MutableStateFlow(value = CreateReviewUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
//        when (action) {
//
//        }
    }
}