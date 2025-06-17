package com.domain.repository_interface

import com.domain.entity.Company

interface CompanyDetailRepository {
    suspend fun getCompanyInfo(
        companyID: Int
    ): Company
}