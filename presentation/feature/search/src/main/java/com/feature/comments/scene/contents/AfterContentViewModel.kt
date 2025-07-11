package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.CompactCompany
import com.domain.usecase.SearchCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AfterContentUIState(
    val searchedCompanies: List<CompactCompany> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalCount: Int = 0,
    val currentPage: Int = 0,
    val hasNext: Boolean = true,
    val isLastPage: Boolean = false,
    val isLoadingMore: Boolean = false
)

@HiltViewModel
class AfterContentViewModel @Inject constructor(
    private val searchCompaniesUseCase: SearchCompaniesUseCase
) : ViewModel() {
    enum class Action {
        DidUpdateSearchQuery,
        DidRequestLoadMore
    }

    private val _uiState = MutableStateFlow(AfterContentUIState())
    val uiState: StateFlow<AfterContentUIState> = _uiState.asStateFlow()

    fun handleAction(action: Action, query: String? = null) {
        when (action) {
            Action.DidUpdateSearchQuery -> {
                query?.let {
                    searchCompanies(it)
                }
            }
            Action.DidRequestLoadMore -> {
                query?.let {
                    loadMoreCompanies(it)
                }
            }
        }
    }

    private fun searchCompanies(query: String) {
        if (query.isBlank()) {
            clearSearchResults()
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null,
                    currentPage = 0,
                    hasNext = true,
                    isLastPage = false
                )
                
                val result = searchCompaniesUseCase.searchCompanies(
                    keyword = query,
                    latitude = 37.5665,
                    longitude = 126.9780,
                    size = 10,
                    page = 0
                )

                _uiState.value = _uiState.value.copy(
                    totalCount = result.totalCount,
                    searchedCompanies = result.companies,
                    hasNext = result.hasNext,
                    isLastPage = !result.hasNext,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "검색 중 오류가 발생했습니다.",
                    isLoading = false
                )
            }
        }
    }

    private fun loadMoreCompanies(query: String) {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || currentState.isLastPage) return

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isLoadingMore = true)
                val nextPage = currentState.currentPage + 1
                
                val result = searchCompaniesUseCase.searchCompanies(
                    keyword = query,
                    latitude = 37.5665,
                    longitude = 126.9780,
                    size = 10,
                    page = nextPage
                )
                
                val updatedList = currentState.searchedCompanies.toMutableList().apply {
                    addAll(result.companies)
                }
                
                _uiState.value = currentState.copy(
                    totalCount = result.totalCount,
                    searchedCompanies = updatedList,
                    currentPage = nextPage,
                    hasNext = result.hasNext,
                    isLastPage = !result.hasNext,
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    error = e.message ?: "추가 데이터 로딩 중 오류가 발생했습니다.",
                    isLoadingMore = false
                )
            }
        }
    }

    private fun clearSearchResults() {
        _uiState.value = AfterContentUIState()
    }
} 