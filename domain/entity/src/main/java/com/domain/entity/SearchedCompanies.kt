package com.domain.entity

data class SearchedCompanies(
    val companies: List<SearchedCompany>,
    val totalCount: Int,
    val hasNext: Boolean,
    val currentPage: Int
)

data class SearchedCompany(
    val id: Long,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String?,
    val distance: Double,
    val following: Boolean
)
