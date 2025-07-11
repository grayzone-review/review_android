package com.data.repository_implementation

import com.data.network.api_service.UpAPIService
import com.domain.repository_interface.ReviewRepository

import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val upAPIService: UpAPIService
): ReviewRepository {
    suspend fun popularReviews(
        latitude: Double,
        longitude: Double
    ) {

    }
}