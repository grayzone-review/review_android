package com.presentation.company_detail.Scene

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor() : ViewModel() {
    enum class Action {
        DidTapFollowingButton,
        DidTapWriteReviewButton
    }

    var isFollowing by mutableStateOf(false)
        private set

    fun handleAction(action: Action) {
        when (action) {
            Action.DidTapFollowingButton -> {
                isFollowing = !isFollowing
            }
            Action.DidTapWriteReviewButton -> {
                isFollowing = !isFollowing
            }
        }
    }


}