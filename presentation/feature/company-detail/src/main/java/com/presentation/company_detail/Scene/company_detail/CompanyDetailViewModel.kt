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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUIState(
    val companyID: Int? = null,
    val company: Company = Company(),
    val reviews: List<Review> = emptyList(),
    val isFullModeList: List<Boolean> = emptyList(),
    val isFollowingCompany: Boolean = false,
    val showBottomSheet: Boolean = false
)

@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val companyDetailUseCase: CompanyDetailUseCase
) : ViewModel() {
    enum class Action {
        GetCompany,
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
    val uiState: DetailUIState get() = _uiState.value

    fun handleAction(action: Action, index: Int? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.GetCompany -> {
                viewModelScope.launch {
                    val result = companyDetailUseCase.getCompanyInfo(companyID = currentState.companyID ?: 0)
                    Log.d("리저", result.toString())
                    _uiState.update { it.copy(company = result) }
                }
            }
            Action.DidTapFollowCompanyButton -> {
                _uiState.update {
                    it.copy(isFollowingCompany = !currentState.isFollowingCompany)
                }
            }
            Action.DidTapWriteReviewButton -> {
                Log.d("버튼탭", "리뷰 버튼이 탭 되었음")
            }
            Action.DidTapLikeReviewButton -> {
                index?.let { wrappedIndex ->
                    val updatedReviews = currentState.reviews.toMutableList()
                    updatedReviews[wrappedIndex] = updatedReviews[wrappedIndex].copy(
                        liked = !updatedReviews[wrappedIndex].liked,
                        likeCount = (if (updatedReviews[wrappedIndex].liked)
                            updatedReviews[wrappedIndex].likeCount - 1
                        else
                            updatedReviews[wrappedIndex].likeCount + 1).coerceAtLeast(0)
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