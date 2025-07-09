package com.data.review_android.di

import com.data.review_android.navigation.NavigationProvider
import com.feature.comments.SearchAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.presentation.company_detail.ReviewAPI
import com.presentation.login.LoginAPI
import com.presentation.main.MainAPI

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideNavigationProvider(
        mainAPI: MainAPI,
        reviewAPI: ReviewAPI,
        searchAPI: SearchAPI,
        loginAPI: LoginAPI
    ): NavigationProvider {
        return NavigationProvider(
            mainAPI = mainAPI,
            reviewAPI = reviewAPI,
            searchAPI = searchAPI,
            loginAPI = loginAPI
        )
    }
}