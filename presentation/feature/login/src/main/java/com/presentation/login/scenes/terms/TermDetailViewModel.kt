package com.presentation.login.scenes.terms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TermDetailUIState(
    val url: String = ""
)

@HiltViewModel
class TermDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val urlArgument = savedStateHandle.get<String>("url") ?: ""
    private var _uiState = MutableStateFlow(value = TermDetailUIState(
        url = urlArgument
    ))
    val uiState = _uiState.asStateFlow()


}