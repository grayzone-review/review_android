package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.Company
import com.domain.entity.SearchedCompany
import com.domain.usecase.CompanyDetailUseCase
import com.domain.usecase.SearchCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingContentViewModel @Inject constructor(
    private val searchCompaniesUseCase: SearchCompaniesUseCase,
    private val companyDetailUseCase: CompanyDetailUseCase
) : ViewModel() {
    enum class Action {
        LoadRecentCompanies,
        DidUpdateSearchBarValue,
        DidTapSearchedCompanyItem,
        DidTapRemoveRecentCompanyButton
    }

    private val _autocompletedCompanies = MutableStateFlow<List<SearchedCompany>>(emptyList())
    val autocompletedCompanies: StateFlow<List<SearchedCompany>> = _autocompletedCompanies.asStateFlow()

    private val _recentCompanies = MutableStateFlow<List<Company>>(emptyList())
    val recentCompany: StateFlow<List<Company>> = _recentCompanies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun handleAction(action: Action, text: String? = null, searchedCompany: SearchedCompany? = null, recentCompany: Company? = null) {
        when (action) {
            Action.DidUpdateSearchBarValue -> {
                text?.let {
                    searchCompanies(query = it)
                }
            }
            Action.LoadRecentCompanies -> {
                val ids: List<Int> = UpDataStoreService.recentCompanyIDs
                    .split(",")
                    .mapNotNull { it.toIntOrNull() }
                getRecentCompanies(companyIDs = ids)
            }

            Action.DidTapSearchedCompanyItem -> {
                searchedCompany?.let {
                    val idToAdd = it.id.toString()
                    val ids: MutableList<String> = UpDataStoreService.recentCompanyIDs
                        .split(",")
                        .filter { it.isNotBlank() }
                        .toMutableList()
                    ids.remove(idToAdd)
                    ids.add(idToAdd)
                    UpDataStoreService.recentCompanyIDs = ids.joinToString(",")
                }
            }

            Action.DidTapRemoveRecentCompanyButton -> {
                recentCompany?.let {
                    val idToRemove = it.id.toString()
                    val ids: MutableList<String> = UpDataStoreService.recentCompanyIDs
                        .split(",")
                        .filter { it.isNotBlank() }
                        .toMutableList()
                    ids.remove(idToRemove)
                    UpDataStoreService.recentCompanyIDs = ids.joinToString(",")

                    val updated = _recentCompanies.value.filterNot { company ->
                        company.id.toString() == idToRemove
                    }
                    _recentCompanies.update { updated }
                }
            }
        }
    }

    private fun getRecentCompanies(companyIDs: List<Int>) {
        viewModelScope.launch {
            val results = companyIDs.map { id ->
                async {
                    try {
                        companyDetailUseCase.getCompanyInfo(companyID = id)
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll()

            val companies = results.filterNotNull()
            _recentCompanies.value = companies
        }
    }

    private fun searchCompanies(query: String) {
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
                    latitude = 37.5665,
                    longitude = 126.9780,
                    size = 20,
                    page = 0
                )
                
                _autocompletedCompanies.value = result.companies
            } catch (e: Exception) {
                _error.value = e.message ?: "검색 중 오류가 발생했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun clearSearchResults() {
        _autocompletedCompanies.value = emptyList()
        _error.value = null
    }
} 