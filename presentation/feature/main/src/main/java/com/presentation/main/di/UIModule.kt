package com.presentation.main.di

import com.presentation.main.MainAPI
import com.presentation.main.MainAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UIModule {

    @Provides
    fun provideMainAPI(): MainAPI {
        return MainAPIImpl()
    }

}