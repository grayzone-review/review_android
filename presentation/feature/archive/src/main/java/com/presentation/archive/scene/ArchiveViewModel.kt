package com.presentation.archive.scene

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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


enum class CollectionTab(val rawValue: String) {
    REVIEW("리뷰"), INTEREST("관심 리뷰"), BOOKMARK("즐겨찾기");

    companion object{
        fun from(name:String?):CollectionTab=
            entries.firstOrNull { it.name.equals(name, true) } ?: REVIEW
    }
}

data class ArchiveUIState(
    val user: User = User(),
    val stats: List<Pair<String, Int>> = emptyList(),
    val selectedTab: CollectionTab = CollectionTab.REVIEW,
    val myReviews: List<MyArchiveReview> = emptyList(),
    val interestReviews: List<MyArchiveReview> = emptyList(),
    val followCompanies: List<MyArchiveCompany> = emptyList(),
    val shouldShowCreateReviewSheet: Boolean = false
)

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        GetMyReviews,
        GetInterestReviews,
        GetCompanyFollowList,
        ShowCreateReviewSheet,
        DismissCrateReviewSheet
    }

    private val tab: CollectionTab = CollectionTab.from(savedStateHandle["tab"])

    private var _uiState = MutableStateFlow(
        value = ArchiveUIState(
            selectedTab = tab
        )
    )
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.OnAppear -> {
                Log.d("탭", "${_uiState.value.selectedTab}")
                viewModelScope.launch {
                    val userResult = userUseCase.userInfo()
                    userResult?.let { bindingResult -> _uiState.update { it.copy(user = bindingResult) } }

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
                    _uiState.update { it.copy(selectedTab = CollectionTab.REVIEW) }
                    val result = userUseCase.myReviews()
                    _uiState.update { it.copy(myReviews = result.reviews) }
                }
            }
            Action.GetInterestReviews -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(selectedTab = CollectionTab.INTEREST) }
                    val result = userUseCase.myInterestReviews()
                    _uiState.update { it.copy(interestReviews = result.reviews) }
                }
            }
            Action.GetCompanyFollowList -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(selectedTab = CollectionTab.BOOKMARK) }
                    val result = userUseCase.myFollowCompanies()
                    _uiState.update { it.copy(followCompanies = result.companies) }
                }
            }
            Action.ShowCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = true) } }
            Action.DismissCrateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = false) } }
        }
    }
}