package com.data.dto.ResponseModel.search

import com.domain.entity.SearchedCompanies
import com.domain.entity.SearchedCompany

data class SearchCompaniesResponseDTO(
    val companies: List<CompanyDTO>,
    val totalCount: Int,
    val hasNext: Boolean,
    val currentPage: Int
)

data class CompanyDTO(
    val id: Long,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String?,   // nullable
    val distance: Double,
    val following: Boolean
)

fun SearchCompaniesResponseDTO.toDomain(): SearchedCompanies = SearchedCompanies(
    companies   = companies.map { it.toDomain() },
    totalCount  = totalCount,
    hasNext     = hasNext,
    currentPage = currentPage
)

private fun CompanyDTO.toDomain() = SearchedCompany(
    id            = id,
    companyName   = companyName,
    companyAddress= companyAddress,
    totalRating   = totalRating,
    reviewTitle   = reviewTitle,
    distance      = distance,
    following     = following
)