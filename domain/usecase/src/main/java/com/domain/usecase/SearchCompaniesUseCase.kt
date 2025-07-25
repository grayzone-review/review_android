package com.domain.usecase

import com.domain.entity.CompactCompanies
import com.domain.repository_interface.SearchCompaniesRepository
import javax.inject.Inject

interface SearchCompaniesUseCase {
    suspend fun searchCompanies(keyword: String, latitude: Double, longitude: Double, size: Int, page: Int): CompactCompanies
    suspend fun nearbyCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies
    suspend fun mainRegionCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies
    suspend fun interestRegionsCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies
}

class SearchCompaniesUseCaseImpl @Inject constructor(
    private val searchCompaniesRepository: SearchCompaniesRepository
) : SearchCompaniesUseCase {
    override suspend fun searchCompanies(keyword: String, latitude: Double, longitude: Double, size: Int, page: Int): CompactCompanies {
        return searchCompaniesRepository.searchCompanies(keyword = keyword, latitude = latitude, longitude = longitude, size = size, page = page)
    }

    override suspend fun nearbyCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies {
        return searchCompaniesRepository.nearbyCompanies(latitude = latitude, longitude = longitude, page = page)
    }

    override suspend fun mainRegionCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies {
        return searchCompaniesRepository.mainRegionCompanies(latitude = latitude, longitude = longitude, page = page)
    }

    override suspend fun interestRegionsCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies {
        return searchCompaniesRepository.interestRegionsCompanies(latitude = latitude, longitude = longitude, page = page)
    }
} 