package com.domain.usecase

import com.domain.entity.Company
import com.domain.entity.FollowCompanyResult
import com.domain.entity.Reviews
import com.domain.repository_interface.CompanyDetailRepository
import javax.inject.Inject

interface CompanyDetailUseCase {
    suspend fun getCompanyInfo(
        companyID: Int
    ): Company
    suspend fun companyReviews(
        companyID: Int
    ): Reviews
    suspend fun followCompany(
        companyID: Int
    ): FollowCompanyResult
    suspend fun unfollowCompany(
        companyID: Int
    ): FollowCompanyResult
}

class CompanyDetailUseCaseImpl @Inject constructor(
    private val companyDetailRepository: CompanyDetailRepository
): CompanyDetailUseCase {
    override suspend fun getCompanyInfo(companyID: Int): Company {
        return companyDetailRepository.getCompanyInfo(companyID = companyID)
    }

    override suspend fun companyReviews(companyID: Int): Reviews {
        return companyDetailRepository.companyReviews(companyID = companyID)
    }

    override suspend fun followCompany(companyID: Int): FollowCompanyResult {
        return companyDetailRepository.followCompany(companyID = companyID)
    }

    override suspend fun unfollowCompany(companyID: Int): FollowCompanyResult {
        return companyDetailRepository.unfollowCompany(companyID = companyID)
    }
}