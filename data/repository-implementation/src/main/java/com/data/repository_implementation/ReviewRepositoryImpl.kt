package com.data.repository_implementation

import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAPIService
import com.domain.entity.ReviewFeed
import com.domain.repository_interface.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val upAPIService: UpAPIService
): ReviewRepository {
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

