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

