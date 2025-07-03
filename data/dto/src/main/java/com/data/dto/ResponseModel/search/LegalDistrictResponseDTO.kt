package com.data.dto.ResponseModel.search

data class LegalDistrictResponseDTO(
    val hasNext: Boolean,
    val currentPage: Int,
    val legalDistricts: List<LegalDistrict>
)

data class LegalDistrict(
    val id: Long,
    val name: String
)