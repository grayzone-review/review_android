package com.feature.comments.scene

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.data.storage.datastore.UpDataStoreService
import com.feature.comments.scene.contents.TagButtonData
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
    enum class SearchInterfaceAction {
        DidUpdateSearchBarValue,
        DidFocusSearchBar,
        DidUnfocusSearchBar,
        DidTapClearButton,
        DidTapCancelButton,
        DidTapIMEDone
    }
    enum class ContentAction {
        DidTapRecentQueryButton,
        DidTapFilterButtons
    }

    private val _searchUISate = MutableStateFlow<SearchUIState>(value = SearchUIState())
    val searchUIState = _searchUISate.asStateFlow()

    fun handleAction(searchInterfaceAction: SearchInterfaceAction, text: String? = null) {
        when (searchInterfaceAction) {
            SearchInterfaceAction.DidUpdateSearchBarValue -> {
                text?.let { newText ->
                    _searchUISate.update { it.copy(searchBarValue = TextFieldValue(text = newText)) }
                }
            }
            SearchInterfaceAction.DidFocusSearchBar -> {
                _searchUISate.update {
                    it.copy(
                        hasFocus = true,
                        phase = SearchPhase.Searching
                    )
                }
            }
            SearchInterfaceAction.DidUnfocusSearchBar -> {
                _searchUISate.update { it.copy(hasFocus = false) }
            }
            SearchInterfaceAction.DidTapClearButton -> {
                _searchUISate.update { it.copy(searchBarValue = TextFieldValue(text = "")) }
            }
            SearchInterfaceAction.DidTapCancelButton -> {
                _searchUISate.update {
                    it.copy(
                        hasFocus = false,
                        phase = SearchPhase.Before
                    )
                }
            }
            SearchInterfaceAction.DidTapIMEDone -> {
                val queries = UpDataStoreService.recentQueries.split(",").toMutableList()
                Log.d("여길드가나?", queries.toString())
                queries.add(index = 0, element = searchUIState.value.searchBarValue.text)
                UpDataStoreService.recentQueries = queries.joinToString(",")
                _searchUISate.update {
                    it.copy(
                        hasFocus = false,
                        phase = SearchPhase.After
                    )
                }
            }
        }
    }

    fun handleAction(contentAction: ContentAction, text: String? = null, tagButtonData: TagButtonData? = null) {
        when (contentAction) {
            ContentAction.DidTapRecentQueryButton -> {
                text?.let { query ->
                    _searchUISate.update {
                        it.copy(
                            searchBarValue = TextFieldValue(text = query),
                            phase = SearchPhase.Searching
                        )
                    }
                }
            }
            ContentAction.DidTapFilterButtons -> {
                tagButtonData?.let { buttonData ->
                    _searchUISate.update {
                        it.copy(
                            searchBarValue = TextFieldValue(text = "#${buttonData.label}"),
                            phase = SearchPhase.After
                        )
                    }
                }
            }
        }
    }

}