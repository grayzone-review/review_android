package com.presentation.main.scene

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.location.UpLocationService
import com.domain.entity.ReviewFeed
import com.domain.usecase.ReviewUseCase
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
    val isShowCreateReviewDialog: Boolean = false,
    val popularFeeds: List<ReviewFeed> = emptyList(),
    val myTownFeeds: List<ReviewFeed> = emptyList(),
    val interestRegionsFeeds: List<ReviewFeed> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        GetPopularFeeds,
        ShowSettingAlert
    }

    private var _uiState = MutableStateFlow(value = MainUIState())
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<MainUIEvent>()
    val event: SharedFlow<MainUIEvent> = _event.asSharedFlow()

    fun handleAction(action: Action) {
        when (action) {
            Action.GetPopularFeeds -> {
                viewModelScope.launch {
                    val (latitude, longitude) = UpLocationService.fetchCurrentLocation() ?: return@launch
                    val popularFeeds = reviewUseCase.popularReviewFeeds(latitude = latitude, longitude = longitude)
                    _uiState.update { it.copy(popularFeeds = popularFeeds) }
                    Log.d("오냐?", "${latitude} ${longitude} / ${popularFeeds.size}")
                }
            }
            Action.ShowSettingAlert -> {
                viewModelScope.launch {
                    _event.emit(MainUIEvent.ShowSettingAlert)
                }
            }
        }
    }
}