package com.domain.usecase

import com.domain.entity.Comment
import com.domain.entity.Comments
import com.domain.entity.LikeReviewResult
import com.domain.entity.Replies
import com.domain.entity.Reply
import com.domain.entity.Review
import com.domain.entity.ReviewFeed
import com.domain.repository_interface.ReviewRepository
import javax.inject.Inject

interface ReviewUseCase {
    suspend fun likeReview(reviewID: Int): LikeReviewResult
    suspend fun unlikeReview(reviewID: Int): LikeReviewResult
    suspend fun reviewComments(reviewID: Int, page: Int): Comments?
    suspend fun writeComment(reviewID: Int, content: String, isSecret: Boolean): Comment?
    suspend fun commentReplies(commentID: Int, page: Int): Replies?
    suspend fun writeReply(commentID: Int, content: String, isSecret: Boolean): Reply?
    suspend fun popularReviewFeeds(latitude: Double, longitude: Double): List<ReviewFeed>?
    suspend fun myTownReviewFeeds(latitude: Double, longitude: Double): List<ReviewFeed>?
    suspend fun interestRegionsReviewFeeds(latitude: Double, longitude: Double): List<ReviewFeed>?
    suspend fun createReview(
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
    ): Review?
}

class ReviewUseCaseImpl @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ReviewUseCase {
    override suspend fun likeReview(
        reviewID: Int
    ): LikeReviewResult {
        return reviewRepository.likeReview(reviewID = reviewID)
    }

    override suspend fun unlikeReview(
        reviewID: Int
    ): LikeReviewResult {
        return reviewRepository.unlikeReview(reviewID = reviewID)
    }

    override suspend fun reviewComments(
        reviewID: Int,
        page: Int
    ): Comments? {
        return reviewRepository.reviewComments(reviewID = reviewID, page = page)
    }

    override suspend fun writeComment(
        reviewID: Int,
        content: String,
        isSecret: Boolean
    ): Comment? {
        return reviewRepository.writeComment(reviewID = reviewID, content = content, isSecret = isSecret)
    }

    override suspend fun commentReplies(
        commentID: Int,
        page: Int
    ): Replies? {
        return reviewRepository.commentReplies(commentID = commentID, page = page)
    }

    override suspend fun writeReply(
        commentID: Int,
        content: String,
        isSecret: Boolean
    ): Reply? {
        return reviewRepository.writeReply(commentID = commentID, content = content, isSecret = isSecret)
    }

    override suspend fun popularReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed>? {
        return reviewRepository.popularReviewFeeds(latitude, longitude)
    }
    
    override suspend fun myTownReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed>? {
        return reviewRepository.myTownReviewFeeds(latitude, longitude)
    }
    
    override suspend fun interestRegionsReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed>? {
        return reviewRepository.interestRegionsReviewFeeds(latitude, longitude)
    }

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
        management: Double,
    ): Review? {
        return reviewRepository.createReview(companyID, advantagePoint, disadvantagePoint, managementFeedback, jobRole, employmentPeriod, welfare, workLifeBalance, salary, companyCulture, management)
    }
}