package com.data.dto.ResponseModel.search

import com.domain.entity.LegalDistrictInfo
import com.domain.entity.LegalDistrictSearchResult

data class LegalDistrictResponseDTO(
    val hasNext: Boolean,
    val currentPage: Int,
    val legalDistricts: List<LegalDistrict>,
)

data class LegalDistrict(
    val id: Long,
    val name: String,
)

fun LegalDistrictResponseDTO.toDomain(): LegalDistrictSearchResult {
    return LegalDistrictSearchResult(
        hasNext = hasNext,
        currentPage = currentPage,
        districts = legalDistricts.map { it.toInfo() }
    )
}

private fun LegalDistrict.toInfo(): LegalDistrictInfo {
    return LegalDistrictInfo(id = id, name = name)
}