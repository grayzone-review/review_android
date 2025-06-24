package com.presentation.main.scene

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class MainUIState(
    val isShowCreateReviewDialog: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {
        DidTapOpenSheet,
        DidTapCloseSheet
    }

    private var _uiState = MutableStateFlow(value = MainUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action) {
        when (action) {
            Action.DidTapCloseSheet -> {
                _uiState.update {
                    it.copy(isShowCreateReviewDialog = false)
                }
            }

            Action.DidTapOpenSheet -> {
                _uiState.update {
                    it.copy(isShowCreateReviewDialog = true)
                }
            }
        }
    }



}