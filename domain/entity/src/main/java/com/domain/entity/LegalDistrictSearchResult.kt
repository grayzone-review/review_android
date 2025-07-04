package com.domain.entity

data class LegalDistrictInfo(
    val id: Long,
    val name: String
)

data class LegalDistrictSearchResult(
    val hasNext: Boolean,
    val currentPage: Int,
    val districts: List<LegalDistrictInfo>
)