package com.presentation.main.scene.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.ReviewFeed
import com.domain.entity.User
import com.domain.usecase.ReviewUseCase
import com.domain.usecase.UserUseCase
import com.presentation.main.NavConstant
import com.team.common.feature_api.error.APIException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FeedUIEvent {
    data class ShowAlert(val error: APIException? = null) : FeedUIEvent
}

data class FeedUIState(
    val section: String = "",
    val user: User = User(),
    val reviewFeeds: List<ReviewFeed> = emptyList(),
    val shouldShowCreateReviewSheet: Boolean = false
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewUseCase: ReviewUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        ShowSettingAlert,
        ShowCreateReviewSheet,
        DismissCrateReviewSheet,
        DidTapLikeReviewButton,
        DidTapCommentButton
    }

    private val section: String = savedStateHandle.get<String>("section") ?: ""

    private val _uiState = MutableStateFlow(
        FeedUIState(section = section)
    )
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<FeedUIEvent>()
    val event = _event.asSharedFlow()

    fun handleAction(action: Action, value: Any? = null) {
        val currentState = _uiState.value
        when (action) {
            Action.OnAppear -> {
                val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                viewModelScope.launch {
                    val userResult = userUseCase.userInfo()
                    _uiState.update { it.copy(user = userResult) }
                    try {
                        val feedsResult = when (section) {
                            NavConstant.Section.MyTown.value -> reviewUseCase.myTownReviewFeeds(latitude = latitude, longitude = longitude)
                            NavConstant.Section.InterestRegions.value -> reviewUseCase.interestRegionsReviewFeeds(latitude = latitude, longitude = longitude)
                            else -> reviewUseCase.popularReviewFeeds(latitude = latitude, longitude = longitude)
                        }
                        feedsResult?.let { bindingResult -> _uiState.update { it.copy(reviewFeeds = bindingResult) }  }
                    } catch (error: APIException) {
                        _event.emit(FeedUIEvent.ShowAlert(error))
                    }
                }
            }
            Action.ShowSettingAlert -> {
                /* TODO: 얼럿 보이기 */
            }
            Action.ShowCreateReviewSheet, Action.DismissCrateReviewSheet -> {
                _uiState.update { it.copy(shouldShowCreateReviewSheet = !currentState.shouldShowCreateReviewSheet) }
            }
            Action.DidTapLikeReviewButton -> {
                val targetReviewID = value as? Int ?: return
                val targetReviewIndex = currentState.reviewFeeds.indexOfFirst { it.review.id == targetReviewID }
                val targetReview = currentState.reviewFeeds[targetReviewIndex]
                viewModelScope.launch {
                    val shouldLike = !targetReview.review.liked
                    if (shouldLike) {
                        reviewUseCase.likeReview(reviewID = targetReview.review.id)
                    } else {
                        reviewUseCase.unlikeReview(reviewID = targetReview.review.id)
                    }
                    val updatedReview = targetReview.review.copy(
                        liked = shouldLike,
                        likeCount = (targetReview.review.likeCount) + if (shouldLike) 1 else -1
                    )
                    _uiState.update { state ->
                        val updatedFeeds = state.reviewFeeds.toMutableList().apply {
                            this[targetReviewIndex] = state.reviewFeeds[targetReviewIndex]
                                .copy(review = updatedReview)
                        }
                        state.copy(reviewFeeds = updatedFeeds)
                    }
                }
            }
            Action.DidTapCommentButton -> TODO()
        }
    }
}