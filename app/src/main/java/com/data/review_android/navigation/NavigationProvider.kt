package com.data.review_android.navigation

import com.feature.comments.SearchAPI
import com.presentation.company_detail.ReviewAPI

data class NavigationProvider(
    val reviewAPI: ReviewAPI,
    val searchAPI: SearchAPI
)
