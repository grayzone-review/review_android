package com.domain.entity

data class MyArchiveCompanies(
    val companies: List<MyArchiveCompany> = emptyList(),
    val hasNext: Boolean = false,
    val currentPage: Int = 0
)

data class MyArchiveCompany(
    val id: Int,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String? = null
)