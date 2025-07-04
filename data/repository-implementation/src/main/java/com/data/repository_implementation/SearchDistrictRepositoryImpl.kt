package com.data.repository_implementation

import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.domain.entity.LegalDistrictSearchResult
import com.domain.repository_interface.SearchDistrictRepository
import javax.inject.Inject

class SearchDistrictRepositoryImpl @Inject constructor(
    private val upAPIService: UpAPIService
): SearchDistrictRepository {
    override suspend fun searchLegalDistrict(
        keyword: String,
        page: Int
    ): LegalDistrictSearchResult {
        val responseDTO = upAPIService.searchLegalDistrict(keyword = keyword, page = page)
        return responseDTO.data?.toDomain()!!
    }
}