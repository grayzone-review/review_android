package com.presentation.company_detail.di

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.presentation.company_detail.ReviewAPI
import com.presentation.company_detail.ReviewAPIImpl
import dagger.Module


@InstallIn(SingletonComponent::class)
@Module
object UIModule {

    @Provides
    fun provideReviewAPI(): ReviewAPI {
        return ReviewAPIImpl()
    }

}