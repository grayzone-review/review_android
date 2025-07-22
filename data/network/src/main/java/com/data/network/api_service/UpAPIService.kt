package com.data.network.api_service

import com.data.dto.ResponseModel.search.CompanyInfoResponseDTO
import com.data.dto.ResponseModel.search.CompanyReviewsResponseDTO
import com.data.dto.ResponseModel.search.LegalDistrictResponseDTO
import com.data.dto.ResponseModel.search.ReviewFeedResponseDTO
import com.data.dto.ResponseModel.search.SearchCompaniesResponseDTO
import com.data.network.endpoint.UpEndpoint
import com.data.network.response.APIResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UpAPIService {
    @GET(UpEndpoint.Path.SEARCH)
    suspend fun searchCompanies(
        @QueryMap params: Map<String, @JvmSuppressWildcards Any>
    ): APIResponse<SearchCompaniesResponseDTO>

    @GET(UpEndpoint.Path.COMPANY_INFO)
    suspend fun getCompanyInfo(
        @Path(UpEndpoint.Query.COMPANY_ID) companyID: Int
    ): APIResponse<CompanyInfoResponseDTO>

    @GET(UpEndpoint.Path.COMPANY_REVIEW)
    suspend fun getCompanyReviews(
        @Path(UpEndpoint.Query.COMPANY_ID) companyID: Int
    ): APIResponse<CompanyReviewsResponseDTO>

    @POST(UpEndpoint.Path.FOLLOW_COMPANY)
    suspend fun followCompany(
        @Path(UpEndpoint.Query.COMPANY_ID) companyId: Int
    ): APIResponse<Unit>

    @DELETE(UpEndpoint.Path.FOLLOW_COMPANY)
    suspend fun unfollowCompany(
        @Path(UpEndpoint.Query.COMPANY_ID) companyId: Int
    ): APIResponse<Unit>

    @GET(UpEndpoint.Path.SEARCH_LEGAL_DISTRICTS)
    suspend fun searchLegalDistrict(
        @Query(UpEndpoint.Query.KEYWORD) keyword: String,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<LegalDistrictResponseDTO>

    @GET(UpEndpoint.Path.POPULAR_REVIEWS)
    suspend fun popularReviews(
        @Query(UpEndpoint.Query.LATITUDE) latitude: Double,
        @Query(UpEndpoint.Query.LONGITUDE) longitude: Double
    ): APIResponse<ReviewFeedResponseDTO>

    @GET(UpEndpoint.Path.MY_TOWN_REVIEWS)
    suspend fun myTownReviews(
        @Query(UpEndpoint.Query.LATITUDE) latitude: Double,
        @Query(UpEndpoint.Query.LONGITUDE) longitude: Double
    ): APIResponse<ReviewFeedResponseDTO>

    @GET(UpEndpoint.Path.INTEREST_REGIONS_REVIEWS)
    suspend fun interestRegionsReviews(
        @Query(UpEndpoint.Query.LATITUDE) latitude: Double,
        @Query(UpEndpoint.Query.LONGITUDE) longitude: Double
    ): APIResponse<ReviewFeedResponseDTO>
}