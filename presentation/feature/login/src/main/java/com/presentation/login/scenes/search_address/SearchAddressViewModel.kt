package com.presentation.login.scenes.search_address

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchAddressUIState(
    val query: String = ""
)

class SearchAddressViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        UpdateSearchQuery
    }

    private val _uiState = MutableStateFlow(SearchAddressUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.UpdateSearchQuery -> {
                val query = value as? String ?: return
                _uiState.update { it.copy(query = query) }
            }
        }
    }

}