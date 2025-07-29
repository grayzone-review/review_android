package com.domain.entity

data class Company(
    val id: Int,
    val companyName: String,
    val permissionDate: String,
    val siteFullAddress: String,
    val roadNameAddress: String,
    val latitude: Double,
    val longitude: Double,
    val totalRating: Double,
    val following: Boolean
)

fun Company.toCompactForWriteReview(): CompactCompany {
    return CompactCompany(
        id = id,
        companyName = companyName,
        companyAddress = siteFullAddress,
        totalRating = totalRating,
        reviewTitle = "",
        distance = 0.0,
        following = following
    )
}