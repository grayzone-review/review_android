package com.data.network.api_service

import com.data.dto.ResponseModel.search.CompanyInfoResponseDTO
import com.data.dto.ResponseModel.search.SearchCompaniesResponseDTO
import com.data.network.endpoint.UpEndpoint
import com.data.network.response.APIResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface UpAPIService {
    @GET(UpEndpoint.Path.SEARCH)
    suspend fun searchCompanies(
        @QueryMap params: Map<String, @JvmSuppressWildcards Any>
    ): APIResponse<SearchCompaniesResponseDTO>


    @GET(UpEndpoint.Path.COMPANY_INFO)
    suspend fun getCompanyInfo(
        @Path("companyID") companyID: Int
    ): APIResponse<CompanyInfoResponseDTO>
}