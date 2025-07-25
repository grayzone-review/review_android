package com.feature.comments.scene

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.data.storage.datastore.UpDataStoreService
import com.feature.comments.scene.contents.TagButtonType
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
    val phase: SearchPhase = SearchPhase.Before,
    val selectedTagButtonType: TagButtonType? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    enum class SearchInterfaceAction {
        DidUpdateSearchBarValue,
        DidFocusSearchBar,
        DidUnFocusSearchBar,
        DidTapClearButton,
        DidTapCancelButton,
        DidTapIMEDone
    }
    enum class ContentAction {
        DidTapRecentQueryButton,
        DidTapFilterButtons
    }

    private val _searchUISate = MutableStateFlow(value = SearchUIState())
    val searchUIState = _searchUISate.asStateFlow()

    fun handleAction(searchInterfaceAction: SearchInterfaceAction, value: Any? = null) {
        when (searchInterfaceAction) {
            SearchInterfaceAction.DidUpdateSearchBarValue -> {
                val newQuery = value as? String ?: return
                _searchUISate.update { it.copy(searchBarValue = TextFieldValue(text = newQuery)) }
            }
            SearchInterfaceAction.DidFocusSearchBar -> {
                _searchUISate.update {
                    it.copy(
                        hasFocus = true,
                        selectedTagButtonType = null,
                        phase = SearchPhase.Searching
                    )
                }
            }
            SearchInterfaceAction.DidUnFocusSearchBar -> {
                _searchUISate.update { it.copy(hasFocus = false) }
            }
            SearchInterfaceAction.DidTapClearButton -> {
                _searchUISate.update {
                    it.copy(
                        searchBarValue = TextFieldValue(text = ""),
                        selectedTagButtonType = null,
                        phase = SearchPhase.Searching
                    )
                }
            }
            SearchInterfaceAction.DidTapCancelButton -> {
                _searchUISate.update {
                    it.copy(
                        hasFocus = false,
                        selectedTagButtonType = null,
                        phase = SearchPhase.Before
                    )
                }
            }
            SearchInterfaceAction.DidTapIMEDone -> {
                val queries = UpDataStoreService.recentQueries.split(",").toMutableList()
                queries.add(index = 0, element = searchUIState.value.searchBarValue.text)
                UpDataStoreService.recentQueries = queries.joinToString(",")
                _searchUISate.update {
                    it.copy(
                        hasFocus = false,
                        selectedTagButtonType = null,
                        phase = SearchPhase.After
                    )
                }
            }
        }
    }

    fun handleAction(contentAction: ContentAction, value: Any? = null) {
        val currentState = _searchUISate.value
        when (contentAction) {
            ContentAction.DidTapRecentQueryButton -> {
                val query = value as? String ?: return
                val recentQueries = UpDataStoreService.recentQueries
                    .split(",")
                    .filter { it.isNotBlank() && it != query }
                val newQueries = listOf(query) + recentQueries
                UpDataStoreService.recentQueries = newQueries.joinToString(",")
                _searchUISate.update {
                    it.copy(
                        searchBarValue = TextFieldValue(text = query),
                        selectedTagButtonType = null,
                        phase = SearchPhase.Searching
                    )
                }
            }
            ContentAction.DidTapFilterButtons -> {
                val newSelectedTagButtonType = value as? TagButtonType ?: return
                if (currentState.selectedTagButtonType == newSelectedTagButtonType) {
                    _searchUISate.update {
                        it.copy(
                            searchBarValue = TextFieldValue(text = ""),
                            selectedTagButtonType = null,
                            phase = SearchPhase.After
                        )
                    }
                } else {
                    _searchUISate.update {
                        it.copy(
                            searchBarValue = TextFieldValue(text = "#${newSelectedTagButtonType.label}"),
                            selectedTagButtonType = newSelectedTagButtonType,
                            phase = SearchPhase.After
                        )
                    }
                }
            }
        }
    }

}