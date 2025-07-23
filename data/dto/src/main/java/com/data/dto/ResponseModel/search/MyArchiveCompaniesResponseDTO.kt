package com.data.dto.ResponseModel.search

import com.domain.entity.MyArchiveCompanies
import com.domain.entity.MyArchiveCompany


data class MyArchiveCompaniesResponseDTO(
    val companies: List<MyArchiveCompanyDTO> = emptyList(),
    val hasNext: Boolean = false,
    val currentPage: Int = 0
)

data class MyArchiveCompanyDTO(
    val id: Int,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String? = null
)

fun MyArchiveCompaniesResponseDTO.toDomain(): MyArchiveCompanies =
    MyArchiveCompanies(
        companies = companies.map(MyArchiveCompanyDTO::toDomain),
        hasNext = hasNext,
        currentPage = currentPage
    )

fun MyArchiveCompanyDTO.toDomain(): MyArchiveCompany =
    MyArchiveCompany(
        id = id,
        companyName = companyName,
        companyAddress = companyAddress,
        totalRating = totalRating,
        reviewTitle = reviewTitle
    )
