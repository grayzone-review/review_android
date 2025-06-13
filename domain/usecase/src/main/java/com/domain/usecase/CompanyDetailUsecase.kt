package com.domain.usecase

import com.domain.entity.Company
import com.domain.repository_interface.CompanyDetailRepository
import javax.inject.Inject

interface CompanyDetailUseCase {
    suspend fun getCompanyInfo(
        companyID: Int
    ): Company
}

class CompanyDetailUseCaseImpl @Inject constructor(
    private val companyDetailRepository: CompanyDetailRepository
): CompanyDetailUseCase {
    override suspend fun getCompanyInfo(companyID: Int): Company {
        return companyDetailRepository.getCompanyInfo(companyID = companyID)
    }
}