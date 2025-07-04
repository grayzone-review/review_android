package com.domain.repository_interface

import com.domain.entity.LegalDistrictSearchResult

interface SearchDistrictRepository {
    suspend fun searchLegalDistrict(
        keyword: String,
        page: Int
    ): LegalDistrictSearchResult
}