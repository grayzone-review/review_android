package com.feature.comments.scene

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.data.storage.datastore.UpDataStoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class SearchPhase {
    Before,
    Searching,
    After
}

data class SearchUIState(
    val searchBarValue: TextFieldValue = TextFieldValue(""),
    val hasFocus: Boolean = false,
    val phase: SearchPhase = SearchPhase.Before
)

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidUpdateSearchBarValue,
        DidFocusSearchBar,
        DidUnfocusSearchBar,
        DidTapClearButton,
        DidTapCancelButton,
        DidTapIMEDone,
        DidTapRecentQueryButton
    }

    private val _searchUISate = MutableStateFlow<SearchUIState>(value = SearchUIState())
    val searchUIState = _searchUISate.asStateFlow()

    fun handleAction(action: Action, text: String? = null) {
        when (action) {
            Action.DidUpdateSearchBarValue -> {
                text?.let { newText ->
                    _searchUISate.update { it.copy(searchBarValue = TextFieldValue(text = newText)) }
                }
            }
            Action.DidFocusSearchBar -> {
                _searchUISate.update {
                    it.copy(
                        hasFocus = true,
                        phase = SearchPhase.Searching
                    )
                }
            }
            Action.DidUnfocusSearchBar -> {
                _searchUISate.update { it.copy(hasFocus = false) }
            }
            Action.DidTapClearButton -> {
                _searchUISate.update { it.copy(searchBarValue = TextFieldValue(text = "")) }
            }
            Action.DidTapCancelButton -> {
                _searchUISate.update {
                    it.copy(
                        hasFocus = false,
                        phase = SearchPhase.Before
                    )
                }
            }
            Action.DidTapIMEDone -> {
                val queries = UpDataStoreService.recentQueries.split(",").toMutableList()
                queries.add(index = 0, element = searchUIState.value.searchBarValue.text)
                UpDataStoreService.recentQueries = queries.joinToString(",")
                _searchUISate.update {
                    it.copy(
                        hasFocus = false,
                        phase = SearchPhase.After
                    )
                }
            }
            Action.DidTapRecentQueryButton -> {
                text?.let { query ->
                    _searchUISate.update { it.copy(
                        searchBarValue = TextFieldValue(text = query),
                        phase = SearchPhase.After
                    ) }
                }
            }
        }
    }
}