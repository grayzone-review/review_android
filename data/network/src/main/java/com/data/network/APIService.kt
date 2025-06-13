package com.data.network

import com.data.dto.ResponseModel.search.SearchCompaniesResponseDTO
import com.data.network.endpoint.Endpoint
import com.data.network.response.APIResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIService {
    @GET(Endpoint.Path.SEARCH)
    suspend fun searchCompanies(
        @QueryMap params: Map<String, @JvmSuppressWildcards Any>
    ): APIResponse<SearchCompaniesResponseDTO>
}