package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.SearchedCompany
import com.domain.entity.SearchedCompanies
import com.domain.usecase.SearchCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AfterContentViewModel @Inject constructor(
    private val searchCompaniesUseCase: SearchCompaniesUseCase
) : ViewModel() {
    private val _searchedCompanies = MutableStateFlow<SearchedCompanies>(
        SearchedCompanies(
            companies = emptyList(),
            totalCount = 0,
            hasNext = false,
            currentPage = 1
        )
    )
    val searchedCompanies = _searchedCompanies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun searchCompanies(query: String) {
        if (query.isBlank()) {
            clearSearchResults()
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val result = searchCompaniesUseCase.searchCompanies(
                    keyword = query,
                    latitude = 37.5665, // TODO: 실제 위치 정보로 대체 필요
                    longitude = 126.9780, // TODO: 실제 위치 정보로 대체 필요
                    size = 20,
                    page = 0
                )
                
                _searchedCompanies.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "검색 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSearchResults() {
        _searchedCompanies.value = SearchedCompanies(
            companies = emptyList(),
            totalCount = 0,
            hasNext = false,
            currentPage = 1
        )
        _error.value = null
    }
} 