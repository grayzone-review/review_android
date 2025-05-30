package com.domain.entity

data class Company(
    val companyName: String,
    val following: Boolean,
    val id: Int,
    val permissionDate: String,
    val roadNameAddress: String,
    val siteFullAddress: String,
    val totalRating: Double,
    val xcoordinate: Double,
    val ycoordinate: Double
)