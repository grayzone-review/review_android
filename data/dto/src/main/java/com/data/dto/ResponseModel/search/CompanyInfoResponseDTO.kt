package com.data.dto.ResponseModel.search

import com.domain.entity.Company

data class CompanyInfoResponseDTO(
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

fun CompanyInfoResponseDTO.toDomain(): Company = Company(
    id,
    companyName,
    permissionDate,
    siteFullAddress,
    roadNameAddress,
    latitude,
    longitude,
    totalRating,
    following
)