package com.presentation.archive.scene

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.CompactCompany
import com.domain.entity.Ratings
import com.domain.entity.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ArchiveUIState(
    val stats: List<Pair<String, Int>> = emptyList(),
    val wroteReviews: List<Review> = emptyList(),
    val interestReviews: List<Review> = emptyList(),
    val followCompanies: List<CompactCompany> = emptyList()
)

@HiltViewModel
class ArchiveViewModel @Inject constructor(
) : ViewModel() {
    enum class Action {
        GetStats,
        GetWroteReviews,
        GetInterestReviews,
        GetCompanyFollowList
    }

    private var _uiState = MutableStateFlow(value = ArchiveUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.GetStats -> {
                viewModelScope.launch {
                    val result = getMockStats()
                    _uiState.update { it.copy(stats = result) }
                }
            }
            Action.GetWroteReviews -> {
                viewModelScope.launch {
                    val result = getMockReviews()
                    _uiState.update { it.copy(wroteReviews = result) }
                }
            }
            Action.GetInterestReviews -> {
                viewModelScope.launch {
                    val result = getMockReviews()
                    _uiState.update { it.copy(interestReviews = result) }
                }
            }
            Action.GetCompanyFollowList -> {
                viewModelScope.launch {
                    val result = getMockCompactCompanies()
                    _uiState.update { it.copy(followCompanies = result) }
                }
            }
        }
    }

}


private suspend fun getMockStats() : List<Pair<String, Int>> {
    delay(200)
    return listOf(
        "작성 리뷰 수" to 2,
        "도움이 됐어요" to 2,
        "즐겨찾기" to 3
    )
}
private suspend fun getMockCompactCompanies(): List<CompactCompany> {
    delay(200)
    return List(10) { idx ->
        CompactCompany(
            id = idx.toLong(),
            companyName = "테스트상호${idx + 1}",
            companyAddress = "서울특별시 종로구 관철동 ${100 + idx}",
            totalRating = 3.0 + (idx % 2) * 0.5,      // 3.0 또는 3.5
            reviewTitle = "리뷰 제목 ${idx + 1}",
            distance = 1.0 + idx,                    // 1.0, 2.0, … 10.0
            following = false
        )
    }
}
private suspend fun getMockReviews(): List<Review> {
    delay(200)
    return List(10) { idx ->
        Review(
            id = idx,
            ratings = mockRatings(idx),
            author = "tester${idx + 1}@mail.com",
            title = "예약이 많아 포트폴리오 쌓기엔 좋지만,\n예약 사이 간격이 촘촘해 쉬는 시간이 부족했어요",
            advantagePoint = "장점 예시 ${idx + 1}",
            disadvantagePoint = "단점 예시 ${idx + 1}",
            managementFeedback = "경영진 피드백 예시 ${idx + 1}",
            jobRole = "네일아티스트",
            employmentPeriod = "1년미만",
            createdAt = "2025-0${(idx % 6) + 1}.15",
            likeCount = idx * 3,
            commentCount = idx * 2,
            liked = idx % 2 == 0
        )
    }
}
private fun mockRatings(idx:Int): Ratings {
    return Ratings(
        companyCulture = 3.0 + (idx % 3) * 0.2,   // 3.0, 3.2, 3.4 …
        management = 2.5 + (idx % 4) * 0.25, // 2.5, 2.75 …
        salary = 2.0 + (idx % 5) * 0.3,  // 2.0, 2.3 …
        welfare = 3.0 + (idx % 2) * 0.5,  // 3.0, 3.5
        workLifeBalance = 2.8 + (idx % 3) * 0.4  // 2.8, 3.2 …
    )
}