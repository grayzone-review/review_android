package com.domain.repository_interface

import com.domain.entity.Company
import com.domain.entity.FollowCompanyResult

interface CompanyDetailRepository {
    suspend fun getCompanyInfo(
        companyID: Int
    ): Company
    suspend fun followCompany(
        companyID: Int
    ): FollowCompanyResult
    suspend fun unfollowCompany(
        companyID: Int
    ): FollowCompanyResult
}