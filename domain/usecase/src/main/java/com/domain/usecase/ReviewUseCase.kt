package com.domain.usecase

import com.domain.entity.ReviewFeed
import com.domain.repository_interface.ReviewRepository
import javax.inject.Inject

interface ReviewUseCase {
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

class ReviewUseCaseImpl @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ReviewUseCase {
    override suspend fun popularReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed> {
        return reviewRepository.popularReviewFeeds(latitude, longitude)
    }
    
    override suspend fun myTownReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed> {
        return reviewRepository.myTownReviewFeeds(latitude, longitude)
    }
    
    override suspend fun interestRegionsReviewFeeds(
        latitude: Double,
        longitude: Double
    ): List<ReviewFeed> {
        return reviewRepository.interestRegionsReviewFeeds(latitude, longitude)
    }
}