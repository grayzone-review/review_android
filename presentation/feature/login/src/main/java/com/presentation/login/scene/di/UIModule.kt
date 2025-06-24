package com.presentation.login.scene.di

import com.presentation.login.scene.LoginAPI
import com.presentation.login.scene.LoginAPIImpl
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