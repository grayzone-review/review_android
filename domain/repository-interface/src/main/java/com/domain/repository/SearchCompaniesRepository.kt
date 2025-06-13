package com.domain.repository

import com.domain.entity.SearchedCompanies

interface SearchCompaniesRepository {
    suspend fun searchCompanies(
        keyword: String,
        latitude: Double,
        longitude: Double,
        size: Int,
        page: Int
    ): SearchedCompanies
} 