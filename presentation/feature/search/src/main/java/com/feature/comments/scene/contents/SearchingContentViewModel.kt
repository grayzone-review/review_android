package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.location.UpLocationService
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.CompactCompany
import com.domain.entity.Company
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

    private val _autocompletedCompanies = MutableStateFlow<List<CompactCompany>>(emptyList())
    val autocompletedCompanies: StateFlow<List<CompactCompany>> = _autocompletedCompanies.asStateFlow()

    private val _recentCompanies = MutableStateFlow<List<Company>>(emptyList())
    val recentCompany: StateFlow<List<Company>> = _recentCompanies.asStateFlow()
    private val _isLoading = MutableStateFlow(false)

    fun handleAction(action: Action, text: String? = null, compactCompany: CompactCompany? = null, recentCompany: Company? = null) {
        when (action) {
            Action.DidUpdateSearchBarValue -> {
                if (_isLoading.value) return
                text?.let { query ->
                    if (query.isBlank()) {
                        _autocompletedCompanies.value = emptyList()
                        return@let
                    }
                    viewModelScope.launch {
                        _isLoading.value = true

                        val (latitude, longitude) = runCatching {
                            UpDataStoreService.lastKnownLocation
                                .split(',')
                                .map { it.toDouble() }
                                .let { it[0] to it[1] }
                        }.getOrElse {
                            UpLocationService.DEFAULT_SEOUL_TOWNHALL
                        }
                        val result = searchCompaniesUseCase.searchCompanies(
                            keyword = query,
                            latitude = latitude,
                            longitude = longitude,
                            size = 20,
                            page = 0
                        )
                        _autocompletedCompanies.value = result.companies
                        _isLoading.value = false
                    }
                }
            }
            Action.LoadRecentCompanies -> {
                val ids: List<Int> = UpDataStoreService.recentCompanyIDs
                    .split(",")
                    .mapNotNull { it.toIntOrNull() }
                getRecentCompanies(companyIDs = ids)
            }

            Action.DidTapSearchedCompanyItem -> {
                compactCompany?.let{
                    val idToAdd = it.id.toString()
                    val ids = UpDataStoreService.recentCompanyIDs
                        .split(",")
                        .filter{ it.isNotBlank() }
                        .toMutableList()
                    ids.remove(idToAdd)
                    ids.add(0,idToAdd)
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
} 