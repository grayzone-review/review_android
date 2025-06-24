package com.data.review_android.di

import android.content.Context
import com.data.review_android.R
import com.data.review_android.navigation.NavigationProvider
import com.feature.comments.SearchAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.presentation.company_detail.ReviewAPI
import com.presentation.login.scene.LoginAPI
import com.presentation.main.MainAPI
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    // 키 빼기
    @Provides
    @Named("NATIVE_APP_KEY")
    fun provideNativeAppKey(@ApplicationContext context: Context): String {
        return context.getString(R.string.kakao_map_api_key)
    }

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