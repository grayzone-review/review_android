package com.presentation.main.scene

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import create_review_dialog.CreateReviewDialogViewModel
import create_review_dialog.CreateReviewUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class MainUIState(
    val isShowCreateReviewDialog: Boolean = true
)

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {

    }

    private var _uiState = MutableStateFlow(value = MainUIState())
    val uiState = _uiState.asStateFlow()




}