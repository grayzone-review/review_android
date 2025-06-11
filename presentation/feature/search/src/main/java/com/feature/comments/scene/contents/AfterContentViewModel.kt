package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import com.domain.entity.SearchedCompany
import com.domain.entity.SearchedCompanies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AfterContentViewModel @Inject constructor() : ViewModel() {
    private val _searchedCompanies = MutableStateFlow<SearchedCompanies>(
        SearchedCompanies(
            companies = emptyList(),
            totalCount = 0,
            hasNext = false,
            currentPage = 1
        )
    )
    val searchedCompanies = _searchedCompanies.asStateFlow()

    private fun searchCompanies(query: String) {
        _searchedCompanies.value = SearchedCompanies(
            companies = listOf(
                SearchedCompany(
                    id = 1,
                    companyName = "스타벅스 석촌역점",
                    companyAddress = "서울특별시 송파구 백제고분로 358 1층",
                    totalRating = 4.0,
                    reviewTitle = "복지가 좋고 경력 쌓기에 좋은 회사",
                    distance = 0.8,
                    following = false
                ),
                SearchedCompany(
                    id = 2,
                    companyName = "투썸플레이스 잠실점",
                    companyAddress = "서울특별시 송파구 올림픽로 240",
                    totalRating = 4.2,
                    reviewTitle = "분위기 좋고 직원들이 친절함",
                    distance = 1.2,
                    following = true
                ),
                SearchedCompany(
                    id = 3,
                    companyName = "이디야커피 석촌호수점",
                    companyAddress = "서울특별시 송파구 석촌호수로 135",
                    totalRating = 3.8,
                    reviewTitle = null,
                    distance = 0.5,
                    following = false
                )
            ),
            totalCount = 3,
            hasNext = false,
            currentPage = 1
        )
    }

    private fun clearSearchResults() {
        _searchedCompanies.value = SearchedCompanies(
            companies = emptyList(),
            totalCount = 0,
            hasNext = false,
            currentPage = 1
        )
    }
} 