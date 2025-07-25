package com.presentation.company_detail.Scene.company_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Company
import com.domain.entity.Review
import com.domain.usecase.CompanyDetailUseCase
import com.domain.usecase.ReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUIState(
    val companyID: Int? = null,
    val company: Company = Company(),
    val reviews: List<Review> = emptyList(),
    val isFullModeList: List<Boolean> = emptyList(),
    val showBottomSheet: Boolean = false,
    val currentPage: Int = 0,
    val hasNext: Boolean = false,
    val isLoading: Boolean = false,
    val shouldShowCreateReviewSheet: Boolean = false
)

@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val companyDetailUseCase: CompanyDetailUseCase,
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        GetCompany,
        GetReviews,
        GetReviewsMore,
        DidTapFollowCompanyButton,
        DidTapLikeReviewButton,
        DidTapReviewCard,
        DidTapCommentButton,
        DidCloseBottomSheet,
        ShowCreateReviewSheet,
        DismissCreateReviewSheet
    }

    private val companyIDArgument: Int? = savedStateHandle
        .get<String>("companyId")
        ?.takeIf { it.isNotBlank() }
        ?.toIntOrNull()

    private var _uiState = MutableStateFlow(DetailUIState(
        companyID = companyIDArgument?.takeIf { it > 0 }
    ))
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, index: Int? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.GetCompany -> {
                viewModelScope.launch {
                    val result = companyDetailUseCase.getCompanyInfo(companyID = currentState.companyID ?: 0)
                    _uiState.update { it.copy(company = result) }
                }
            }
            Action.GetReviews -> {
                if (currentState.isLoading) return
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    val result = companyDetailUseCase.companyReviews(companyID = currentState.companyID ?: 0, page = 0)
                    _uiState.update {
                        it.copy(
                            reviews = result.reviews ?: emptyList(),
                            isFullModeList = it.isFullModeList + List(result.reviews?.size ?: 0) { false },
                            isLoading = false,
                            currentPage = currentState.currentPage + 1,
                            hasNext = result.hasNext ?: false
                        )
                    }
                }
            }
            Action.GetReviewsMore -> {
                if(currentState.isLoading || !currentState.hasNext) return
                val nextPage = currentState.currentPage
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    val result = companyDetailUseCase.companyReviews(companyID = currentState.companyID ?: 0, page = nextPage)
                    _uiState.update {
                        it.copy(
                            reviews = result.reviews ?: emptyList(),
                            isFullModeList = it.isFullModeList + List(result.reviews?.size ?: 0) { false },
                            isLoading = false,
                            currentPage = currentState.currentPage + 1,
                            hasNext = result.hasNext ?: false
                        )
                    }
                }
            }
            Action.DidTapFollowCompanyButton -> {
                viewModelScope.launch {
                    currentState.company.following?.let { followStatus ->
                        val result = if (!followStatus) {
                            companyDetailUseCase.followCompany(companyID = currentState.companyID ?: 0)
                        } else {
                            companyDetailUseCase.unfollowCompany(companyID = currentState.companyID ?: 0)
                        }
                        if (result.success) {
                            val company = companyDetailUseCase.getCompanyInfo(companyID = currentState.companyID ?: 0)
                            _uiState.update { it.copy(company = company) }
                        }
                    }
                }
            }
            Action.DidTapLikeReviewButton -> {
                index?.let { idx ->
                    viewModelScope.launch {
                        val targetReview = currentState.reviews[idx]
                        val shouldLike = targetReview.liked != true
                        val result = if (shouldLike) {
                            reviewUseCase.likeReview(reviewID = targetReview.id ?: 0)
                        } else {
                            reviewUseCase.unlikeReview(reviewID = targetReview.id ?: 0)
                        }
                        if (result.success) {
                            val updatedReview = targetReview.copy(
                                liked = shouldLike,
                                likeCount = (targetReview.likeCount ?: 0) + if (shouldLike) 1 else -1
                            )
                            _uiState.update { state ->
                                state.copy(
                                    reviews = state.reviews.toMutableList().also { it[idx] = updatedReview }
                                )
                            }
                        }
                    }
                }
            }
            Action.DidTapReviewCard -> {
                index?.let {
                    val updatedFullModeList = currentState.isFullModeList.toMutableList()
                    updatedFullModeList[index] = !updatedFullModeList[index]
                    _uiState.update { it.copy(isFullModeList = updatedFullModeList) }
                }
            }
            Action.DidTapCommentButton, Action.DidCloseBottomSheet -> {
                _uiState.update { it.copy(showBottomSheet = !currentState.showBottomSheet) }
            }
            Action.ShowCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = true) } }
            Action.DismissCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = false) } }
        }
    }
}