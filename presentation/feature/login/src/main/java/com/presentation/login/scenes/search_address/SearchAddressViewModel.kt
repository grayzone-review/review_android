package com.presentation.login.scenes.search_address

import androidx.lifecycle.ViewModel
import com.domain.entity.Region
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchAddressUIState(
    val query: String = ""
)

class SearchAddressViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        UpdateQueryFromSearching,
        UpdateQueryFromLocation
    }

    private val _uiState = MutableStateFlow(SearchAddressUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.UpdateQueryFromSearching -> {
                val query = value as? String ?: return
                _uiState.update { it.copy(query = query) }
            }
            Action.UpdateQueryFromLocation -> {
                val region = value as? Region ?: return
                val si = region.region_1depth_name
                val gu = region.region_2depth_name
                val dong = region.region_3depth_name
                val regionQuery = "$si $gu $dong"
                _uiState.update { it.copy(query = regionQuery) }
            }
        }
    }

}