package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.CompactCompany
import com.domain.usecase.CompanyDetailUseCase
import com.domain.usecase.SearchCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AfterContentUIState(
    val searchedCompanies: List<CompactCompany> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalCount: Int = 0,
    val currentPage: Int = 0,
    val hasNext: Boolean = false
)

@HiltViewModel
class AfterContentViewModel @Inject constructor(
    private val searchCompaniesUseCase: SearchCompaniesUseCase,
    private val companyDetailUseCase: CompanyDetailUseCase
) : ViewModel() {
    enum class Action {
        DidUpdateSearchQuery,
        DidRequestLoadMore,
        DidTapFollowCompanyButton
    }

    private val _uiState = MutableStateFlow(AfterContentUIState())
    val uiState: StateFlow<AfterContentUIState> = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.DidUpdateSearchQuery -> {
                val query = value as? String ?: return
                val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                if (query.isBlank()) { clearSearchResults(); return }
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                    val result = searchCompaniesUseCase.searchCompanies(
                        keyword = query,
                        latitude = latitude,
                        longitude = longitude,
                        size = 10,
                        page = 0
                    )
                    _uiState.update {
                        it.copy(
                            totalCount = result.totalCount,
                            searchedCompanies = result.companies,
                            hasNext = result.hasNext,
                            isLoading = false
                        )
                    }
                }
            }
            Action.DidRequestLoadMore -> {
                val query = value as? String ?: return
                val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                if (currentState.isLoading || !currentState.hasNext) return
                viewModelScope.launch {
                    val nextPage = currentState.currentPage + 1
                    _uiState.update { it.copy(isLoading = true) }
                    val result = searchCompaniesUseCase.searchCompanies(
                        keyword = query,
                        latitude = latitude,
                        longitude = longitude,
                        size = 10,
                        page = nextPage
                    )
                    _uiState.update {
                        it.copy(
                            totalCount = result.totalCount,
                            searchedCompanies = currentState.searchedCompanies + result.companies,
                            hasNext = result.hasNext,
                            isLoading = false
                        )
                    }
                }
            }
            Action.DidTapFollowCompanyButton -> {
                viewModelScope.launch {
                    val tappedCompany = value as? CompactCompany ?: return@launch
                    val targetIndex = currentState.searchedCompanies.indexOfFirst { it.id == tappedCompany.id }
                    val targetCompanyFollowingState = currentState.searchedCompanies[targetIndex].following
                    val result = if (!targetCompanyFollowingState) {
                        companyDetailUseCase.followCompany(companyID = tappedCompany.id)
                    } else {
                        companyDetailUseCase.unfollowCompany(companyID = tappedCompany.id)
                    }
                    if (result.success) {
                        val updated = currentState.searchedCompanies
                            .toMutableList()
                            .apply { this[targetIndex] = this[targetIndex].copy(following = !targetCompanyFollowingState) }
                        _uiState.update { it.copy(searchedCompanies = updated) }
                    }
                }
            }
        }
    }

    private fun clearSearchResults() {
        _uiState.value = AfterContentUIState()
    }
} 