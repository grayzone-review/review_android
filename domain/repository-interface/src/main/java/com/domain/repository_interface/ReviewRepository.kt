package com.domain.repository_interface

import com.domain.entity.ReviewFeed

interface ReviewRepository {
    suspend fun popularReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed>
    suspend fun myTownReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed>
    suspend fun interestRegionsReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed>
}