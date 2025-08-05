package com.data.dto.ResponseModel.search

import com.domain.entity.CompactCompanies
import com.domain.entity.CompactCompany

data class SearchCompaniesResponseDTO(
    val companies: List<CompanyDTO>,
    val totalCount: Int,
    val hasNext: Boolean,
    val currentPage: Int
)

data class CompanyDTO(
    val id: Int,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String?,
    val distance: Double,
    val following: Boolean
)

fun SearchCompaniesResponseDTO.toDomain(): CompactCompanies = CompactCompanies(
    companies   = companies.map { it.toDomain() },
    totalCount  = totalCount,
    hasNext     = hasNext,
    currentPage = currentPage
)

private fun CompanyDTO.toDomain() = CompactCompany(
    id            = id,
    companyName   = companyName,
    companyAddress= companyAddress,
    totalRating   = totalRating,
    reviewTitle   = reviewTitle,
    distance      = distance,
    following     = following
)