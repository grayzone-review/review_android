package com.domain.entity

data class Company(
    val id: Int? = null,
    val companyName: String? = null,
    val permissionDate: String? = null,
    val siteFullAddress: String? = null,
    val roadNameAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val totalRating: Double? = null,
    val following: Boolean? = null
)