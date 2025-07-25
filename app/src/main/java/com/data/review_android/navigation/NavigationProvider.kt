package com.data.review_android.navigation

import com.feature.comments.SearchAPI
import com.presentation.archive.ArchiveAPI
import com.presentation.company_detail.CompanyDetailAPI
import com.presentation.login.LoginAPI
import com.presentation.main.MainAPI
import com.presentation.mypage.MyPageAPI
import com.presentation.onboarding.OnBoardingAPI

data class NavigationProvider(
    val mainAPI: MainAPI,
    val companyDetailAPI: CompanyDetailAPI,
    val searchAPI: SearchAPI,
    val loginAPI: LoginAPI,
    val archiveAPI: ArchiveAPI,
    val onBoardingAPI: OnBoardingAPI,
    val myPageAPI: MyPageAPI
)
