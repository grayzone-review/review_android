package com.domain.entity

data class User(
    val nickname: String? = null,
    val mainRegion: LegalDistrictInfo? = null,
    val interestedRegions: List<LegalDistrictInfo>? = null
)
