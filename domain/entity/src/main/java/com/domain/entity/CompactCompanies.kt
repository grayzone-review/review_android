package com.domain.entity

data class CompactCompanies(
    val companies: List<CompactCompany>,
    val totalCount: Int,
    val hasNext: Boolean,
    val currentPage: Int
)

data class CompactCompany(
    val id: Int,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String?,
    val distance: Double,
    val following: Boolean
)
