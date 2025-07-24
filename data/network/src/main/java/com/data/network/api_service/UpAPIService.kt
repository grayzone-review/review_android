package com.data.network.api_service

import RequestModel.CreateCompanyReviewRequestModel
import RequestModel.ModifyUserRequestModel
import RequestModel.ReportRequestModel
import RequestModel.ResignRequestModel
import RequestModel.WriteCommentRequestModel
import RequestModel.WriteReplyRequestModel
import com.data.dto.ResponseModel.search.CommentDto
import com.data.dto.ResponseModel.search.CommentsDTO
import com.data.dto.ResponseModel.search.CompanyInfoResponseDTO
import com.data.dto.ResponseModel.search.CompanyReviewDTO
import com.data.dto.ResponseModel.search.CompanyReviewsResponseDTO
import com.data.dto.ResponseModel.search.LegalDistrictResponseDTO
import com.data.dto.ResponseModel.search.MyArchiveCompaniesResponseDTO
import com.data.dto.ResponseModel.search.MyArchiveReviewsResponseDTO
import com.data.dto.ResponseModel.search.RepliesResponseDTO
import com.data.dto.ResponseModel.search.ReplyDto
import com.data.dto.ResponseModel.search.ReviewFeedResponseDTO
import com.data.dto.ResponseModel.search.SearchCompaniesResponseDTO
import com.data.dto.ResponseModel.search.UserInfoResponseDTO
import com.data.dto.ResponseModel.search.UserInteractionResponseDTO
import com.data.network.endpoint.UpEndpoint
import com.data.network.response.APIResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
        @Path(UpEndpoint.Query.COMPANY_ID) companyID: Int,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<CompanyReviewsResponseDTO>

    @POST(UpEndpoint.Path.COMPANY_REVIEW)
    suspend fun createCompanyReview(
        @Path(UpEndpoint.Query.COMPANY_ID) companyID: Int,
        @Body requestModel: CreateCompanyReviewRequestModel
    ): APIResponse<CompanyReviewDTO>

    @POST(UpEndpoint.Path.FOLLOW_COMPANY)
    suspend fun followCompany(
        @Path(UpEndpoint.Query.COMPANY_ID) companyId: Int
    ): APIResponse<Unit>

    @DELETE(UpEndpoint.Path.FOLLOW_COMPANY)
    suspend fun unfollowCompany(
        @Path(UpEndpoint.Query.COMPANY_ID) companyId: Int
    ): APIResponse<Unit>

    @POST(UpEndpoint.Path.LIKE_REVIEW)
    suspend fun likeReview(
        @Path(UpEndpoint.Query.REVIEW_ID) reviewId: Int
    ): APIResponse<Unit>

    @DELETE(UpEndpoint.Path.LIKE_REVIEW)
    suspend fun unlikeReview(
        @Path(UpEndpoint.Query.REVIEW_ID) reviewId: Int
    ): APIResponse<Unit>

    @GET(UpEndpoint.Path.REVIEW_COMMENT)
    suspend fun reviewComments(
        @Path(UpEndpoint.Query.REVIEW_ID) reviewId: Int,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<CommentsDTO>

    @POST(UpEndpoint.Path.REVIEW_COMMENT)
    suspend fun writeComment(
        @Path(UpEndpoint.Query.REVIEW_ID) reviewId: Int,
        @Body requestModel: WriteCommentRequestModel
    ): APIResponse<CommentDto>

    @GET(UpEndpoint.Path.COMMENT_REPLIES)
    suspend fun commentReplies(
        @Path(UpEndpoint.Query.COMMENT_ID) commentId: Int,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<RepliesResponseDTO>

    @POST(UpEndpoint.Path.COMMENT_REPLIES)
    suspend fun writeReply(
        @Path(UpEndpoint.Query.COMMENT_ID) commentId: Int,
        @Body requestModel: WriteReplyRequestModel
    ): APIResponse<ReplyDto>

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

    @GET(UpEndpoint.Path.MY_INTERACTION_COUNT)
    suspend fun myInteractionCounts(
    ): APIResponse<UserInteractionResponseDTO>

    @GET(UpEndpoint.Path.MY_REVIEWS)
    suspend fun myReviews(
    ): APIResponse<MyArchiveReviewsResponseDTO>

    @GET(UpEndpoint.Path.MY_INTEREST_REVIEWS)
    suspend fun myInterestReviews(
    ): APIResponse<MyArchiveReviewsResponseDTO>

    @GET(UpEndpoint.Path.MY_FOLLOW)
    suspend fun myFollowCompanies(
    ): APIResponse<MyArchiveCompaniesResponseDTO>

    @GET(UpEndpoint.Path.NEARBY_COMPANY)
    suspend fun nearbyCompanies(
        @Query(UpEndpoint.Query.LATITUDE) latitude: Double,
        @Query(UpEndpoint.Query.LONGITUDE) longitude: Double,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<SearchCompaniesResponseDTO>

    @GET(UpEndpoint.Path.MAIN_REGION_COMPANY)
    suspend fun mainRegionCompanies(
        @Query(UpEndpoint.Query.LATITUDE) latitude: Double,
        @Query(UpEndpoint.Query.LONGITUDE) longitude: Double,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<SearchCompaniesResponseDTO>

    @GET(UpEndpoint.Path.INTEREST_REGIONS_COMPANY)
    suspend fun interestRegionsCompanies(
        @Query(UpEndpoint.Query.LATITUDE) latitude: Double,
        @Query(UpEndpoint.Query.LONGITUDE) longitude: Double,
        @Query(UpEndpoint.Query.PAGE) page: Int
    ): APIResponse<SearchCompaniesResponseDTO>

    @GET(UpEndpoint.Path.MY_INFO)
    suspend fun userInfo(
    ): APIResponse<UserInfoResponseDTO>

    @PUT(UpEndpoint.Path.MY_INFO)
    suspend fun modifyUserInfo(
        @Body requestModel: ModifyUserRequestModel
    ): APIResponse<Unit>

    @DELETE(UpEndpoint.Path.MY_INFO)
    suspend fun resign(
        @Body requestModel: ResignRequestModel
    ): APIResponse<Unit>

    @PUT(UpEndpoint.Path.REPORT)
    suspend fun report(
        @Body requestModel: ReportRequestModel
    ): APIResponse<Unit>

}