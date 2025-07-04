package com.data.repository_implementation

import RequestModel.SearchCompaniesRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.APIService
import com.data.network.mapper.SearchCompaniesRequestMapper
import com.data.network.response.mapOrThrow
import com.domain.entity.SearchedCompanies
import com.domain.repository_interface.SearchCompaniesRepository
import javax.inject.Inject

class SearchCompaniesRepositoryImpl @Inject constructor(
    private val apiService: APIService,
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
        val responseDTO = apiService.searchCompanies(requestDTO)
        return responseDTO.mapOrThrow { dto -> dto.toDomain() }
    }
}