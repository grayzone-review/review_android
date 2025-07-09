package com.domain.usecase

import com.domain.entity.LegalDistrictSearchResult
import com.domain.repository_interface.SearchDistrictRepository
import javax.inject.Inject

interface SearchDistrictUseCase {
    suspend fun searchLegalDistrict(
        keyword: String,
        page: Int
    ): LegalDistrictSearchResult
}

class SearchDistrictUseCaseImpl @Inject constructor(
    private val searchDistrictRepository: SearchDistrictRepository
) : SearchDistrictUseCase {
    override suspend fun searchLegalDistrict(
        keyword: String,
        page: Int
    ): LegalDistrictSearchResult {
        return searchDistrictRepository.searchLegalDistrict(keyword, page)
    }
}

