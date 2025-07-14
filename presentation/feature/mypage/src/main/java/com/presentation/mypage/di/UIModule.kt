package com.presentation.mypage.di

import com.presentation.mypage.MyPageAPI
import com.presentation.mypage.MyPageAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UIModule {
    @Provides
    fun provideMyPageAPI(): MyPageAPI {
        return MyPageAPIImpl()
    }
}