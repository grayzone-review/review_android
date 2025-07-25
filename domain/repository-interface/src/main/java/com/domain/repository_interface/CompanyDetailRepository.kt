package com.domain.repository_interface

import com.domain.entity.Company
import com.domain.entity.FollowCompanyResult
import com.domain.entity.Reviews

interface CompanyDetailRepository {
    suspend fun getCompanyInfo(
        companyID: Int
    ): Company
    suspend fun companyReviews(
        companyID: Int,
        page: Int
    ): Reviews
    suspend fun followCompany(
        companyID: Int
    ): FollowCompanyResult
    suspend fun unfollowCompany(
        companyID: Int
    ): FollowCompanyResult
}