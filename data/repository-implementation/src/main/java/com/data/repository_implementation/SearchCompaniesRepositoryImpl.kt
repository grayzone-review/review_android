package com.data.repository_implementation

import RequestModel.SearchCompaniesRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.data.network.mapper.SearchCompaniesRequestMapper
import com.domain.entity.SearchedCompanies
import com.domain.repository_interface.SearchCompaniesRepository
import javax.inject.Inject

class SearchCompaniesRepositoryImpl @Inject constructor(
    private val upApiService: UpAPIService,
    private val searchCompaniesRequestMapper: SearchCompaniesRequestMapper
): SearchCompaniesRepository {
    override suspend fun searchCompanies(
        keyword: String,
        latitude: Double,
        longitude: Double,
        size: Int,
        page: Int
    ): SearchedCompanies {
        val requestModel = SearchCompaniesRequestModel(keyword, latitude, longitude, size, page)
        val requestDTO = searchCompaniesRequestMapper.toDTO(requestModel)
        val responseDTO = upApiService.searchCompanies(requestDTO)
        return responseDTO.data?.toDomain()!!
    }
}