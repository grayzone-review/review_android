package com.data.repository_implementation

import RequestModel.CreateCompanyReviewRequestModel
import RequestModel.RatingsRequestModel
import RequestModel.WriteCommentRequestModel
import RequestModel.WriteReplyRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.domain.entity.Comment
import com.domain.entity.Comments
import com.domain.entity.LikeReviewResult
import com.domain.entity.Replies
import com.domain.entity.Reply
import com.domain.entity.Review
import com.domain.entity.ReviewFeed
import com.domain.repository_interface.ReviewRepository
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.toErrorAction
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val upAPIService: UpAPIService
): ReviewRepository {
    override suspend fun createReview(
        companyID: Int,
        advantagePoint: String,
        disadvantagePoint: String,
        managementFeedback: String,
        jobRole: String,
        employmentPeriod: String,
        welfare: Double,
        workLifeBalance: Double,
        salary: Double,
        companyCulture: Double,
        management: Double
    ): Review {
        val requestDTO = CreateCompanyReviewRequestModel(
            advantagePoint = advantagePoint,
            disadvantagePoint = disadvantagePoint,
            managementFeedback = managementFeedback,
            jobRole = jobRole,
            employmentPeriod = employmentPeriod,
            ratings = RatingsRequestModel(
                welfare = welfare,
                workLifeBalance = workLifeBalance,
                salary = salary,
                companyCulture = companyCulture,
                management = management
            )
        )
        val result = upAPIService.createCompanyReview(companyID = companyID, requestModel = requestDTO)
        return result.data?.toDomain()!!
    }

    override suspend fun likeReview(
        reviewID: Int
    ): LikeReviewResult {
        val responseDTO = upAPIService.likeReview(reviewId = reviewID)
        responseDTO.code?.let { throw APIException(action = it.toErrorAction(), message = responseDTO.message) }
        return LikeReviewResult(message = responseDTO.message, success = responseDTO.success)
    }

    override suspend fun unlikeReview(
        reviewID: Int
    ): LikeReviewResult {
        val responseDTO = upAPIService.unlikeReview(reviewId = reviewID)
        responseDTO.code?.let { throw APIException(action = it.toErrorAction(), message = responseDTO.message) }
        return LikeReviewResult(message = responseDTO.message, success = responseDTO.success)
    }

    override suspend fun reviewComments(
        reviewID: Int,
        page: Int
    ): Comments {
        val result = upAPIService.reviewComments(reviewId = reviewID, page = page)
        return result.data?.toDomain()!!
    }

    override suspend fun writeComment(
        reviewID: Int,
        content: String,
        isSecret: Boolean
    ): Comment {
        val requestDTO = WriteCommentRequestModel(comment = content, secret = isSecret)
        val result = upAPIService.writeComment(reviewId = reviewID, requestModel = requestDTO)
        return result.data?.toDomain()!!
    }

    override suspend fun commentReplies(
        commentID: Int,
        page: Int
    ): Replies {
        val result = upAPIService.commentReplies(commentId = commentID, page = page)
        return result.data?.toDomain()!!
    }
    
    override suspend fun writeReply(
        commentID: Int,
        content: String,
        isSecret: Boolean
    ): Reply {
        val requestDTO = WriteReplyRequestModel(comment = content, secret = isSecret)
        val result = upAPIService.writeReply(commentId = commentID, requestModel = requestDTO)
        return result.data?.toDomain()!!
    }
    
    override suspend fun popularReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed> {
        val result = upAPIService.popularReviews(latitude = latitude, longitude = longitude)
        return result.data!!.toDomain()
    }

    override suspend fun myTownReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed> {
        val result = upAPIService.myTownReviews(latitude = latitude, longitude = longitude)
        return result.data!!.toDomain()
    }

    override suspend fun interestRegionsReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed> {
        val result = upAPIService.interestRegionsReviews(latitude = latitude, longitude = longitude)
        return result.data!!.toDomain()
    }
}

