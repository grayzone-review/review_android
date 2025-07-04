package com.data.review_android.navigation

import com.feature.comments.SearchAPI
import com.presentation.company_detail.ReviewAPI
import com.presentation.main.MainAPI

data class NavigationProvider(
    val mainAPI: MainAPI,
    val reviewAPI: ReviewAPI,
    val searchAPI: SearchAPI
)
