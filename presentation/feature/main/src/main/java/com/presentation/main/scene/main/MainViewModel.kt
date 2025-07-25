package com.presentation.main.scene.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.ReviewFeed
import com.domain.entity.User
import com.domain.usecase.ReviewUseCase
import com.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface MainUIEvent {
    data object ShowSettingAlert : MainUIEvent
}

data class MainUIState(
    val user: User = User(),
    val isShowCreateReviewDialog: Boolean = false,
    val isShowSettingAlertDialog: Boolean = false,
    val popularFeeds: List<ReviewFeed> = emptyList(),
    val myTownFeeds: List<ReviewFeed> = emptyList(),
    val interestRegionsFeeds: List<ReviewFeed> = emptyList(),
    val shouldShowCreateReviewSheet: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        OnAppear,
        GetFeeds,
        ShowSettingAlert,
        DismissSettingAlert,
        ShowCreateReviewSheet,
        DismissCreateReviewSheet
    }

    private var _uiState = MutableStateFlow(value = MainUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MainUIEvent>()
    val event: SharedFlow<MainUIEvent> = _event.asSharedFlow()

    fun handleAction(action: Action) {
        when (action) {
            Action.OnAppear -> {
                viewModelScope.launch {
                    val result = userUseCase.userInfo()
                    _uiState.update { it.copy(user = result) }
                }
            }
            Action.GetFeeds -> {
                viewModelScope.launch {
                    val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                    val popularFeeds = reviewUseCase.popularReviewFeeds(latitude = latitude, longitude = longitude)
                    val myTownFeeds = reviewUseCase.myTownReviewFeeds(latitude = latitude, longitude = longitude)
                    val interestRegionsFeeds = reviewUseCase.interestRegionsReviewFeeds(latitude = latitude, longitude = longitude)
                    _uiState.update {
                        it.copy(
                            popularFeeds = popularFeeds,
                            myTownFeeds = myTownFeeds,
                            interestRegionsFeeds = interestRegionsFeeds
                        )
                    }
                }
            }
            Action.ShowSettingAlert -> { _uiState.update { it.copy(isShowSettingAlertDialog = true) } }
            Action.DismissSettingAlert -> { _uiState.update { it.copy(isShowSettingAlertDialog = false) } }
            Action.ShowCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = true) } }
            Action.DismissCreateReviewSheet -> { _uiState.update { it.copy(shouldShowCreateReviewSheet = false) } }
        }
    }
}