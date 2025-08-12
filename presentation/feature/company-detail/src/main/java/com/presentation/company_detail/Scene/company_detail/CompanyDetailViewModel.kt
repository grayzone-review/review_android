package com.presentation.company_detail.Scene.company_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.entity.Company
import com.domain.entity.Review
import com.domain.usecase.CompanyDetailUseCase
import com.domain.usecase.ReviewUseCase
import com.team.common.feature_api.error.APIException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CompanyDetailUIEvent {
    data class ShowAlert(val error: APIException? = null) : CompanyDetailUIEvent
}

data class DetailUIState(
    val companyID: Int? = null,
    val company: Company? = null,
    val reviews: List<Review> = emptyList(),
    val isFullModeList: List<Boolean> = emptyList(),
    val showBottomSheet: Boolean = false,
    val currentPage: Int = 0,
    val hasNext: Boolean = false,
    val isLoading: Boolean = false,
    val shouldShowCreateReviewSheet: Boolean = false,
    val tappedCommentReview: Review? = null
)

@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val companyDetailUseCase: CompanyDetailUseCase,
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
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
    private val _event = MutableSharedFlow<CompanyDetailUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.OnAppear -> {
                viewModelScope.launch {
                    Log.d("[CompanyDetailScene - OnAppear]", "기업번호:$companyIDArgument")
                    _uiState.update { it.copy(isLoading = true) }
                    try {
                        val companyResult = companyDetailUseCase.getCompanyInfo(companyID = currentState.companyID ?: 0)
                        companyResult?.let { company -> _uiState.update { it.copy(company = company) } }

                        val reviewsResult = companyDetailUseCase.companyReviews(companyID = currentState.companyID ?: 0, page = 0)
                        reviewsResult?.let { bindingResult ->
                            _uiState.update {
                                it.copy(
                                    reviews = bindingResult.reviews,
                                    isFullModeList = it.isFullModeList + List(bindingResult.reviews.size) { false },
                                    isLoading = false,
                                    currentPage = currentState.currentPage + 1,
                                    hasNext = bindingResult.hasNext
                                )
                            }
                        }
                    } catch (error: APIException) {
                        _event.emit(CompanyDetailUIEvent.ShowAlert(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
            Action.GetReviewsMore -> {
                if(currentState.isLoading || !currentState.hasNext) return
                val nextPage = currentState.currentPage
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    try {
                        val result = companyDetailUseCase.companyReviews(companyID = currentState.companyID ?: 0, page = nextPage)
                        result?.let { bindingResult ->
                            _uiState.update {
                                it.copy(
                                    reviews = currentState.reviews + bindingResult.reviews,
                                    isFullModeList = it.isFullModeList + List(bindingResult.reviews.size) { false },
                                    isLoading = false,
                                    currentPage = currentState.currentPage + 1,
                                    hasNext = bindingResult.hasNext
                                )
                            }
                        }
                    } catch (error: APIException) {
                        _event.emit(CompanyDetailUIEvent.ShowAlert(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
            Action.DidTapFollowCompanyButton -> {
                viewModelScope.launch {
                    try {
                        if (currentState.company?.following == false) {
                            companyDetailUseCase.followCompany(companyID = currentState.companyID ?: 0)
                        } else {
                            companyDetailUseCase.unfollowCompany(companyID = currentState.companyID ?: 0)
                        }
                        val company = companyDetailUseCase.getCompanyInfo(companyID = currentState.companyID ?: 0)
                        _uiState.update { it.copy(company = company) }
                    } catch (error: APIException) {
                        _event.emit(CompanyDetailUIEvent.ShowAlert(error))
                    }
                }
            }
            Action.DidTapLikeReviewButton -> {
                val index = value as? Int ?: return
                viewModelScope.launch {
                    val targetReview = currentState.reviews[index]
                    val shouldLike = !targetReview.liked
                    try {
                        if (shouldLike) {
                            reviewUseCase.likeReview(reviewID = targetReview.id)
                        } else {
                            reviewUseCase.unlikeReview(reviewID = targetReview.id)
                        }
                        val updatedReview = targetReview.copy(
                            liked = shouldLike,
                            likeCount = (targetReview.likeCount) + if (shouldLike) 1 else -1
                        )
                        _uiState.update {
                            it.copy(reviews = currentState.reviews.toMutableList().also { current -> current[index] = updatedReview })
                        }
                    } catch (error: APIException) {
                        _event.emit(CompanyDetailUIEvent.ShowAlert(error))
                    }
                }
            }
            Action.DidTapReviewCard -> {
                val index = value as? Int ?: return
                val updatedFullModeList = currentState.isFullModeList.toMutableList()
                updatedFullModeList[index] = !updatedFullModeList[index]
                _uiState.update { it.copy(isFullModeList = updatedFullModeList) }
            }
            Action.DidTapCommentButton -> {
                val tappedReview = value as? Review ?: return
                _uiState.update {
                    it.copy(showBottomSheet = true, tappedCommentReview = tappedReview)
                }
            }
            Action.DidCloseBottomSheet -> {
                _uiState.update { it.copy(showBottomSheet = false) }
                viewModelScope.launch {
                    val reviewID = currentState.tappedCommentReview?.id ?: return@launch
                    val companyID = currentState.companyID ?: return@launch

                    var page = 0
                    var targetReview: Review? = null
                    while (true) {
                        val result = companyDetailUseCase.companyReviews(companyID = companyID, page = page) ?: break
                        val reviews = result.reviews.orEmpty()
                        targetReview = reviews.firstOrNull { it.id == reviewID }
                        if (targetReview != null || reviews.isEmpty()) break
                        page++
                    }

                    targetReview?.let { serverReview ->
                        _uiState.update { state ->
                            val idx = state.reviews.indexOfFirst { it.id == serverReview.id }
                            if (idx < 0) return@update state
                            state.copy(
                                reviews = state.reviews.toMutableList().apply { this[idx] = serverReview },
                                tappedCommentReview = null
                            )
                        }
                    }
                }
            }
            Action.ShowCreateReviewSheet -> {
                _uiState.update { it.copy(shouldShowCreateReviewSheet = true) }
            }
            Action.DismissCreateReviewSheet -> {
                _uiState.update { it.copy(shouldShowCreateReviewSheet = false) }
            }
        }
    }
}