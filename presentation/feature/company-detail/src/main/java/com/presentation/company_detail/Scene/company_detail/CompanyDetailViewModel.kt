package com.presentation.company_detail.Scene.company_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Company
import com.domain.entity.Review
import com.domain.usecase.CompanyDetailUseCase
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
    val showBottomSheet: Boolean = false
)

@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val companyDetailUseCase: CompanyDetailUseCase
) : ViewModel() {
    enum class Action {
        GetCompany,
        GetReviews,
        DidTapFollowCompanyButton,
        DidTapWriteReviewButton,
        DidTapLikeReviewButton,
        DidTapReviewCard,
        DidTapCommentButton,
        DidCloseBottomSheet
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
                viewModelScope.launch {
                    val result = companyDetailUseCase.companyReviews(companyID = currentState.companyID ?: 0)
                    result.currentPage
                    result.hasNext
                    _uiState.update { it.copy(
                        reviews = result.reviews ?: emptyList(),
                        isFullModeList = List(result.reviews?.size ?: 0) { false })
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
            Action.DidTapWriteReviewButton -> {
                Log.d("버튼탭", "리뷰 버튼이 탭 되었음")
            }
            Action.DidTapLikeReviewButton -> {
                index?.let { wrappedIndex ->
                    val updatedReviews = currentState.reviews.toMutableList()
                    val isLiked = updatedReviews[wrappedIndex].liked ?: false
                    updatedReviews[wrappedIndex] = updatedReviews[wrappedIndex].copy(
                        liked = isLiked,
                        likeCount = (if (updatedReviews[wrappedIndex].liked == true)
                            updatedReviews[wrappedIndex].likeCount?.minus(1)
                        else
                            updatedReviews[wrappedIndex].likeCount?.plus(1))?.coerceAtLeast(0)
                    )
                    _uiState.update { it.copy(reviews = updatedReviews) }
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
        }
    }
}