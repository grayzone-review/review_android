package com.data.repository_implementation

import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.data.network.mapper.CompanyDetailRequestMapper
import com.domain.entity.Company
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
}