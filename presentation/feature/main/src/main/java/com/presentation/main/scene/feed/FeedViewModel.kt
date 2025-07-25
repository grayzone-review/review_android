package com.presentation.main.scene.feed

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.ReviewFeed
import com.domain.entity.User
import com.domain.usecase.ReviewUseCase
import com.domain.usecase.UserUseCase
import com.presentation.main.NavConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        DismissCrateReviewSheet
    }

    private val section: String = savedStateHandle.get<String>("section") ?: ""

    private val _uiState = MutableStateFlow(
        FeedUIState(section = section)
    )
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.OnAppear -> {
                val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                viewModelScope.launch {
                    val userResult = userUseCase.userInfo()
                    _uiState.update { it.copy(user = userResult) }

                    val feedsResult = when (section) {
                        NavConstant.Section.MyTown.value -> reviewUseCase.myTownReviewFeeds(latitude = latitude, longitude = longitude)
                        NavConstant.Section.InterestRegions.value -> reviewUseCase.interestRegionsReviewFeeds(latitude = latitude, longitude = longitude)
                        else -> reviewUseCase.popularReviewFeeds(latitude = latitude, longitude = longitude)
                    }
                    _uiState.update { it.copy(reviewFeeds = feedsResult) }
                    Log.d("리저트", feedsResult.size.toString())
                }
            }
            Action.ShowSettingAlert -> {

            }
            Action.ShowCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = true) } }
            Action.DismissCrateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = false) } }
        }
    }
}