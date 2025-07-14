package com.domain.entity

data class User(
    val nickname: String? = null,
    val mainRegion: String? = null,
    val interestedRegions: List<String>? = null
)
