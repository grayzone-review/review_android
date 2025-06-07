package com.feature.comments.di

import com.feature.comments.SearchAPI
import com.feature.comments.SearchAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UIModule {
    @Provides
    fun provideSearchAPI(): SearchAPI {
        return SearchAPIImpl()
    }
}