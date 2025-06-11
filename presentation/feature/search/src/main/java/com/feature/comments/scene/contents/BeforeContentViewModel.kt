package com.feature.comments.scene.contents

import androidx.lifecycle.ViewModel
import com.domain.entity.RecentCompany
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BeforeContentViewModel @Inject constructor() : ViewModel() {
    private val _recentCompanies = MutableStateFlow<List<RecentCompany>>(emptyList())
    val recentCompanies: StateFlow<List<RecentCompany>> = _recentCompanies.asStateFlow()

    init {
        loadRecentCompanies()
    }

    private fun loadRecentCompanies() {
        _recentCompanies.value = listOf(
            RecentCompany(
                id = 1,
                companyName = "스타벅스 석촌점",
                companyAddress = "서울특별시 송파구 백제고분로 358 1층"
            ),
            RecentCompany(
                id = 2,
                companyName = "브로우레시피 잠실새내점",
                companyAddress = "서울특별시 송파구 올림픽로 240"
            ),
            RecentCompany(
                id = 3,
                companyName = "호식이 두마리치킨 사가정점",
                companyAddress = "서울특별시 중랑구 사가정로 123"
            ),
            RecentCompany(
                id = 4,
                companyName = "교촌치킨 서울점",
                companyAddress = "서울특별시 중구 명동길 123"
            )
        )
    }

    private fun removeRecentCompany(companyId: Long) {
        _recentCompanies.update { companies ->
            companies.filter { it.id != companyId }
        }
    }
} 