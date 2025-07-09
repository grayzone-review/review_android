package com.presentation.login.di

import com.presentation.login.LoginAPI
import com.presentation.login.LoginAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object UIModule {
    @Provides
    fun provideLoginAPI(): LoginAPI {
        return LoginAPIImpl()
    }
}