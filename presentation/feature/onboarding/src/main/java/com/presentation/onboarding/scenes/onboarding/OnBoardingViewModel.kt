package com.presentation.onboarding.scenes.onboarding

import androidx.lifecycle.ViewModel
import com.domain.usecase.ReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {

}