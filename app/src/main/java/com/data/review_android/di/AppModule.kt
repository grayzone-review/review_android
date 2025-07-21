package com.data.review_android.di

import com.data.review_android.navigation.NavigationProvider
import com.feature.comments.SearchAPI
import com.presentation.archive.ArchiveAPI
import com.presentation.company_detail.CompanyDetailAPI
import com.presentation.login.LoginAPI
import com.presentation.main.MainAPI
import com.presentation.mypage.MyPageAPI
import com.presentation.onboarding.OnBoardingAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideNavigationProvider(
        mainAPI: MainAPI,
        reviewAPI: CompanyDetailAPI,
        searchAPI: SearchAPI,
        loginAPI: LoginAPI,
        archiveAPI: ArchiveAPI,
        onBoardingAPI: OnBoardingAPI,
        myPageAPI: MyPageAPI
    ): NavigationProvider {
        return NavigationProvider(
            mainAPI = mainAPI,
            companyDetailAPI = reviewAPI,
            searchAPI = searchAPI,
            loginAPI = loginAPI,
            archiveAPI = archiveAPI,
            onBoardingAPI = onBoardingAPI,
            myPageAPI = myPageAPI
        )
    }
}