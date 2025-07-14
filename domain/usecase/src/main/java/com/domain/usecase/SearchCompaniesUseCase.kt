package com.domain.usecase

import com.domain.entity.SearchedCompanies
import com.domain.repository_interface.SearchCompaniesRepository
import javax.inject.Inject

interface SearchCompaniesUseCase {
    suspend fun searchCompanies(
        keyword: String,
        latitude: Double,
        longitude: Double,
        size: Int,
        page: Int
    ): SearchedCompanies
}

class SearchCompaniesUseCaseImpl @Inject constructor(
    private val searchCompaniesRepository: SearchCompaniesRepository
) : SearchCompaniesUseCase {
    override suspend fun searchCompanies(
        keyword: String,
        latitude: Double,
        longitude: Double,
        size: Int,
        page: Int
    ): SearchedCompanies {
        return searchCompaniesRepository.searchCompanies(
            keyword = keyword,
            latitude = latitude,
            longitude = longitude,
            size = size,
            page = page
        )
    }
} 