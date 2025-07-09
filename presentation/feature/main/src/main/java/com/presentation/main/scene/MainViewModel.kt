package com.presentation.main.scene

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class MainUIState(
    val isShowCreateReviewDialog: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {

    }

    private var _uiState = MutableStateFlow(value = MainUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action) {
//        when (action) {
//
//        }
    }



}