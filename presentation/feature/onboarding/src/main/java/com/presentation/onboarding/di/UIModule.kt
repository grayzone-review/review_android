package com.presentation.onboarding.di

import com.presentation.onboarding.OnBoardingAPI
import com.presentation.onboarding.OnBoardingAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UIModule {
    @Provides
    fun provideOnBoardingAPI(): OnBoardingAPI {
        return OnBoardingAPIImpl()
    }
}