package com.data.review_android.di

import com.data.review_android.navigation.NavigationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.presentation.company_detail.ReviewAPI

@InstallIn(SingletonComponent::class)
@Module

object AppModule {

    @Provides
    fun provideNavigationProvider(reviewAPI: ReviewAPI): NavigationProvider {
        return NavigationProvider(reviewAPI)
    }

}