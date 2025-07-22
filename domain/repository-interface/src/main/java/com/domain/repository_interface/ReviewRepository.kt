package com.domain.repository_interface

import com.domain.entity.Comment
import com.domain.entity.Comments
import com.domain.entity.LikeReviewResult
import com.domain.entity.Replies
import com.domain.entity.Reply
import com.domain.entity.ReviewFeed

interface ReviewRepository {
    suspend fun likeReview(reviewID: Int): LikeReviewResult
    suspend fun unlikeReview(reviewID: Int): LikeReviewResult
    suspend fun reviewComments(reviewID: Int, page: Int): Comments
    suspend fun writeComment(reviewID: Int, content: String, isSecret: Boolean): Comment
    suspend fun commentReplies(commentID: Int, page: Int): Replies
    suspend fun writeReply(commentID: Int, content: String, isSecret: Boolean): Reply
    suspend fun popularReviewFeeds(latitude: Double, longitude: Double): List<ReviewFeed>
    suspend fun myTownReviewFeeds(latitude: Double, longitude: Double): List<ReviewFeed>
    suspend fun interestRegionsReviewFeeds(latitude: Double, longitude: Double): List<ReviewFeed>
}