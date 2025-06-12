package com.feature.comments.scene.contents

import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.ViewModel
import com.data.storage.datastore.UpDataStoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BeforeContentViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidAppear,
        DidTapQueryTagDeleteButton,
    }

    private val _recentQueries = MutableStateFlow<List<String>>(emptyList())
    val recentQueries: StateFlow<List<String>> = _recentQueries.asStateFlow()

    fun handleAction(action: Action, index: Int? = null) {
        when (action) {
            Action.DidAppear -> {
                val queries = UpDataStoreService.recentQueries.split(",")
                _recentQueries.update { queries }
            }
            Action.DidTapQueryTagDeleteButton -> {
                index?.let {
                    val queries = UpDataStoreService.recentQueries.split(",").toMutableList()
                    queries.removeAt(index = it)
                    UpDataStoreService.recentQueries = queries.joinToString(",")
                    _recentQueries.update { queries }
                }
            }
        }
    }

    private fun removeRecentCompany(companyId: Long) {
//        _recentQueries.update { companies ->
//
//        }
    }
} 