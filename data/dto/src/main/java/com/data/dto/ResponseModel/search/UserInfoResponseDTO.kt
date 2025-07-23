package com.data.dto.ResponseModel.search

import com.domain.entity.LegalDistrictInfo
import com.domain.entity.User

data class UserInfoResponseDTO(
    val interestedRegions: List<RegionDTO> = emptyList(),
    val nickname: String,
    val mainRegionAddress: String,
    val mainRegionId: Int
)

data class RegionDTO(
    val id: Int,
    val address: String
)

fun UserInfoResponseDTO.toDomain(): User {
    return User(
        nickname = nickname,
        mainRegion = LegalDistrictInfo(id = mainRegionId, name = mainRegionAddress),
        interestedRegions = interestedRegions.map(RegionDTO::toDomain)
    )
}

private fun RegionDTO.toDomain(): LegalDistrictInfo {
    return LegalDistrictInfo(id = id, name = address)
}