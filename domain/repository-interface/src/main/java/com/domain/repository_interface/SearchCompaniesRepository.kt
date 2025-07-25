package com.domain.repository_interface

import com.domain.entity.CompactCompanies

interface SearchCompaniesRepository {
    suspend fun searchCompanies(keyword: String, latitude: Double, longitude: Double, size: Int, page: Int): CompactCompanies
    suspend fun nearbyCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies
    suspend fun mainRegionCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies
    suspend fun interestRegionsCompanies(latitude: Double, longitude: Double, page: Int): CompactCompanies
}
