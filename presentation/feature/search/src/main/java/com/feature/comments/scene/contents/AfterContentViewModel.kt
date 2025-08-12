package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.location.UpLocationService
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.CompactCompany
import com.domain.usecase.CompanyDetailUseCase
import com.domain.usecase.SearchCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AfterContentUIState(
    val searchedCompanies: List<CompactCompany> = emptyList(),
    val isLoading: Boolean = false,
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

    private var searchJob: Job? = null
    private val _uiState = MutableStateFlow(AfterContentUIState())
    val uiState: StateFlow<AfterContentUIState> = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.DidUpdateSearchQuery -> {
                val query = (value as? String)?.trim() ?: return
                if (query.isBlank()) { clearSearchResults(); return }
                // 기 검색 요청 사항이 있다면 취소
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    val (lat, lng) = runCatching {
                        UpDataStoreService.lastKnownLocation
                            .split(',')
                            .map { it.toDouble() }
                            .let { it[0] to it[1] }
                    }.getOrElse {
                        UpLocationService.DEFAULT_SEOUL_TOWNHALL
                    }

                    val tag = TagButtonType.entries.firstOrNull { it.label == query.removePrefix("#") }
                    _uiState.update { // 초기화 후, 다시 검색
                        it.copy(
                            isLoading = true,
                            searchedCompanies = emptyList(),
                            totalCount = 0,
                            currentPage = 0
                        )
                    }
                    val result = when (tag) {
                        TagButtonType.Around   -> searchCompaniesUseCase.nearbyCompanies(lat, lng, page = 0)
                        TagButtonType.MyTown   -> searchCompaniesUseCase.mainRegionCompanies(lat, lng, page = 0)
                        TagButtonType.Interest -> searchCompaniesUseCase.interestRegionsCompanies(lat, lng, page = 0)
                        null -> searchCompaniesUseCase.searchCompanies(keyword = query, latitude = lat, longitude = lng, size = 10, page = 0)
                    }
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
                /* 입력된 쿼리(또는 태그 문자열) 다시 확인 */
                val query=(value as? String)?.trim() ?: return
                if(query.isBlank()||currentState.isLoading||!currentState.hasNext) return

                val (lat, lng) = runCatching {
                    UpDataStoreService.lastKnownLocation
                        .split(',')
                        .map { it.toDouble() }
                        .let { it[0] to it[1] }
                }.getOrElse {
                    UpLocationService.DEFAULT_SEOUL_TOWNHALL
                }
                val tag = TagButtonType.entries.firstOrNull { it.label == query.removePrefix("#") }
                val nextPage = currentState.currentPage + 1
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }

                    val result = when (tag) {
                        TagButtonType.Around   -> searchCompaniesUseCase.nearbyCompanies(lat, lng, page = nextPage)
                        TagButtonType.MyTown   -> searchCompaniesUseCase.mainRegionCompanies(lat, lng, page = nextPage)
                        TagButtonType.Interest -> searchCompaniesUseCase.interestRegionsCompanies(lat, lng, page = nextPage)
                        null                   -> searchCompaniesUseCase.searchCompanies(query, lat, lng,10, nextPage)
                    }
                    _uiState.update {
                        it.copy(
                            searchedCompanies = currentState.searchedCompanies + result.companies,
                            totalCount = result.totalCount,
                            hasNext = result.hasNext,
                            currentPage = nextPage,
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