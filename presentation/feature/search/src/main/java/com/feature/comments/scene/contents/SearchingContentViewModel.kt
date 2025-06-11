package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import com.domain.entity.SearchedCompany
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchingContentViewModel @Inject constructor() : ViewModel() {
    private val _autocompletedCompanies = MutableStateFlow<List<SearchedCompany>>(emptyList())
    val autocompletedCompanies: StateFlow<List<SearchedCompany>> = _autocompletedCompanies.asStateFlow()

    private fun searchCompanies(query: String) {
        _autocompletedCompanies.value = listOf(
            SearchedCompany(
                id = 1L,
                companyName = "븕앉클린더버거조인트 청계천점",
                companyAddress = "서울특별시 중구 무교동 77",
                totalRating = 4.5,
                reviewTitle = "버거가 진짜 미쳤음",
                distance = 0.26,
                following = true
            ),
            SearchedCompany(
                id = 2L,
                companyName = "바스버거 광화문점",
                companyAddress = "서울특별시 중구 무교동 11 광일빌딩 지하1층",
                totalRating = 3.3,
                reviewTitle = null,
                distance = 0.23,
                following = false
            ),
            SearchedCompany(
                id = 3L,
                companyName = "버거운 녀석들",
                companyAddress = "서울특별시 중구 남대문로5가 21-1",
                totalRating = 0.0,
                reviewTitle = null,
                distance = 0.88,
                following = false
            )
        )
    }

    fun clearSearchResults() {
        _autocompletedCompanies.value = emptyList()
    }
} 