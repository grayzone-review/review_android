package com.presentation.main.scene.main

import address_finder.AddressFinderViewModel.Action
import android.util.Log
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
    val interestRegionsFeeds: List<ReviewFeed> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    enum class Action {
        GetUser,
        GetPopularFeeds,
        GetMyTownFeeds,
        GetInterestRegionsFeeds,
        ShowSettingAlert,
        DismissSettingAlert
    }

    private var _uiState = MutableStateFlow(value = MainUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MainUIEvent>()
    val event: SharedFlow<MainUIEvent> = _event.asSharedFlow()

    fun handleAction(action: Action) {
        when (action) {
            Action.GetUser -> {
                viewModelScope.launch {
                    val result = userUseCase.userInfo()
                    _uiState.update { it.copy(user = result) }
                }
            }
            Action.GetPopularFeeds -> {
                viewModelScope.launch {
                    val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                    val popularFeeds = reviewUseCase.popularReviewFeeds(latitude = latitude, longitude = longitude)
                    _uiState.update { it.copy(popularFeeds = popularFeeds) }
                    Log.d("오냐?", "${latitude} ${longitude} / ${popularFeeds.size}")
                }
            }
            Action.GetMyTownFeeds -> {
                viewModelScope.launch {
                    val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                    val myTownFeeds = reviewUseCase.myTownReviewFeeds(latitude = latitude, longitude = longitude)
                    _uiState.update { it.copy(myTownFeeds = myTownFeeds) }
                    Log.d("오냐?", "${latitude} ${longitude} / ${myTownFeeds.size}")
                }
            }
            Action.GetInterestRegionsFeeds -> {
                viewModelScope.launch {
                    val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }
                    val interestRegionsFeeds = reviewUseCase.interestRegionsReviewFeeds(latitude = latitude, longitude = longitude)
                    _uiState.update { it.copy(interestRegionsFeeds = interestRegionsFeeds) }
                    Log.d("오냐?", "${latitude} ${longitude} / ${interestRegionsFeeds.size}")
                }
            }
            Action.ShowSettingAlert -> {
                _uiState.update { it.copy(isShowSettingAlertDialog = true) }
            }
            Action.DismissSettingAlert -> {
                _uiState.update { it.copy(isShowSettingAlertDialog = false) }
            }
        }
    }
}