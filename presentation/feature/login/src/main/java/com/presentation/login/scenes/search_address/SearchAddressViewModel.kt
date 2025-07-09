package com.presentation.login.scenes.search_address

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.domain.entity.Region
import com.presentation.login.scenes.sign_up.navgraph.NavConstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchAddressUIState(
    val query: String = "",
    val mode: String = ""
)

class SearchAddressViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    enum class Action {
        UpdateQueryFromSearching,
        UpdateQueryFromLocation
    }

    private val townArgument = savedStateHandle.get<String>("town") ?: ""
    private val modeArgument = savedStateHandle.get<String>("mode") ?: NavConstant.Mode.MY.value

    private val _uiState = MutableStateFlow(SearchAddressUIState(
        query = townArgument,
        mode = modeArgument
    ))
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