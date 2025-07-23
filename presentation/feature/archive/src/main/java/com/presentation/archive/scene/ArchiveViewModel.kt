package com.presentation.archive.scene

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.MyArchiveCompany
import com.domain.entity.MyArchiveReview
import com.domain.entity.User
import com.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ArchiveUIState(
    val user: User = User(),
    val stats: List<Pair<String, Int>> = emptyList(),
    val myReviews: List<MyArchiveReview> = emptyList(),
    val interestReviews: List<MyArchiveReview> = emptyList(),
    val followCompanies: List<MyArchiveCompany> = emptyList()
)

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        GetUser,
        GetStats,
        GetMyReviews,
        GetInterestReviews,
        GetCompanyFollowList
    }

    private var _uiState = MutableStateFlow(value = ArchiveUIState())
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.GetUser -> {
                viewModelScope.launch {
                    val result = userUseCase.userInfo()
                    _uiState.update { it.copy(user = result) }
                }
            }
            Action.GetStats -> {
                viewModelScope.launch {
                    val result = userUseCase.myInteractionCounts()
                    val stats = listOf(
                        "작성 리뷰 수" to result.myReviewCount,
                        "도움이 됐어요" to result.likeOrCommentReviewCount,
                        "즐겨찾기" to result.followCompanyCount
                    )
                    _uiState.update { it.copy(stats = stats) }
                }
            }
            Action.GetMyReviews -> {
                viewModelScope.launch {
                    val result = userUseCase.myReviews()
                    _uiState.update { it.copy(myReviews = result.reviews) }
                }
            }
            Action.GetInterestReviews -> {
                viewModelScope.launch {
                    val result = userUseCase.myInterestReviews()
                    _uiState.update { it.copy(interestReviews = result.reviews) }
                }
            }
            Action.GetCompanyFollowList -> {
                viewModelScope.launch {
                    val result = userUseCase.myFollowCompanies()
                    _uiState.update { it.copy(followCompanies = result.companies) }
                }
            }
        }
    }
}