package com.data.repository_implementation

import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.data.network.mapper.CompanyDetailRequestMapper
import com.domain.entity.Company
import com.domain.entity.FollowCompanyResult
import com.domain.entity.Reviews
import com.domain.repository_interface.CompanyDetailRepository
import javax.inject.Inject

class CompanyDetailRepositoryImpl @Inject constructor(
    private val upApiService: UpAPIService,
    private val companyDetailRequestMapper: CompanyDetailRequestMapper
): CompanyDetailRepository {
    override suspend fun getCompanyInfo(companyID: Int): Company {
        val responseDTO = upApiService.getCompanyInfo(companyID = companyID)
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun companyReviews(companyID: Int, page: Int): Reviews {
        val responseDTO = upApiService.getCompanyReviews(companyID = companyID, page = page)
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun followCompany(companyID: Int): FollowCompanyResult {
        val responseDTO = upApiService.followCompany(companyId = companyID)
        return FollowCompanyResult(message = responseDTO.message, success = responseDTO.success)
    }

    override suspend fun unfollowCompany(companyID: Int): FollowCompanyResult {
        val responseDTO = upApiService.unfollowCompany(companyId = companyID)
        return FollowCompanyResult(message = responseDTO.message, success = responseDTO.success)
    }
}