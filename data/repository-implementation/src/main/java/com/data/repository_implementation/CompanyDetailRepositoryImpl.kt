package com.data.repository_implementation

import com.data.dto.ResponseModel.search.toDomain
import com.data.network.APIService
import com.data.network.mapper.CompanyDetailRequestMapper
import com.data.network.response.mapOrThrow
import com.domain.entity.Company
import com.domain.repository_interface.CompanyDetailRepository
import javax.inject.Inject

class CompanyDetailRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val companyDetailRequestMapper: CompanyDetailRequestMapper
): CompanyDetailRepository {
    override suspend fun getCompanyInfo(companyID: Int): Company {
        val responseDTO = apiService.getCompanyInfo(companyID = companyID)
        return responseDTO.mapOrThrow { it.toDomain() }
    }
}