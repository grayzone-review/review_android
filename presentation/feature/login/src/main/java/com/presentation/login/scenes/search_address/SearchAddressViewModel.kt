package com.presentation.login.scenes.search_address

import androidx.lifecycle.ViewModel
import javax.inject.Inject

data class SearchAddressUIState(
    val query: String = ""
)

class SearchAddressViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        UpdateSearchQuery
    }

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.UpdateSearchQuery -> {

            }
        }
    }
    
}