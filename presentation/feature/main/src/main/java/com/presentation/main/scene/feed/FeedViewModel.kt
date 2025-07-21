package com.presentation.main.scene.feed

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.storage.datastore.UpDataStoreService
import com.domain.entity.LegalDistrictInfo
import com.domain.entity.ReviewFeed
import com.domain.entity.User
import com.domain.usecase.ReviewUseCase
import com.presentation.main.NavConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUIState(
    val section: String = "",
    val user: User = User(),
    val reviewFeeds: List<ReviewFeed> = emptyList()
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {
    enum class Action {
        GetUser,
        GetFeeds,
        ShowSettingAlert
    }

    private val section: String = savedStateHandle.get<String>("section") ?: ""

    private val _uiState = MutableStateFlow(
        FeedUIState(section = section)
    )
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: Action, value: Any? = null) {
        when (action) {
            Action.GetUser -> {
                viewModelScope.launch {
                    val user = getMockUser()
                    _uiState.update { it.copy(user = user) }
                }
            }
            Action.GetFeeds -> {
                val (latitude, longitude) = UpDataStoreService.lastKnownLocation.split(",").map { it.toDouble() }

                viewModelScope.launch {
                    val result = when (section) {
                        NavConstant.Section.MyTown.value           -> reviewUseCase.myTownReviewFeeds(latitude = latitude, longitude = longitude)
                        NavConstant.Section.InterestRegions.value  -> reviewUseCase.interestRegionsReviewFeeds(latitude = latitude, longitude = longitude)
                        else                                       -> reviewUseCase.popularReviewFeeds(latitude = latitude, longitude = longitude)
                    }
                    _uiState.update { it.copy(reviewFeeds = result) }
                    Log.d("리저트", result.size.toString())
                }
            }
            Action.ShowSettingAlert -> {

            }
        }
    }

    private suspend fun getMockUser(): User {
        delay(200)
        return User(
            nickname = "서현웅",
            mainRegion = LegalDistrictInfo(1, "서울시 중랑구 면목동"),
            interestedRegions = listOf(
                LegalDistrictInfo(1, "서울시 중랑구 면목동"),
                LegalDistrictInfo(2, "서울시 중랑구 중곡동"),
                LegalDistrictInfo(2, "서울시 중랑구 상봉동")
            )
        )
    }
}