package com.data.dto.ResponseModel.search

data class ReviewsResponseDTO(
    val reviews: List<ReviewsItemDTO>,
)

data class ReviewsItemDTO(
    val companyReview: ReviewsCompanyReviewDTO,
    val company: ReviewsCompanyDTO,
)

data class ReviewsCompanyReviewDTO(
    val id: Long,
    val ratings: ReviewsRatingsDTO,
    val author: String,
    val title: String,
    val advantagePoint: String,
    val disadvantagePoint: String,
    val managementFeedback: String,
    val jobRole: String,
    val employmentPeriod: String,
    val createdAt: String,
    val likeCount: Int,
    val commentCount: Int,
    val liked: Boolean,
)

data class ReviewsRatingsDTO(
    val companyCulture: Double,
    val management: Double,
    val welfare: Double,
    val salary: Double,
    val workLifeBalance: Double,
)

data class ReviewsCompanyDTO(
    val id: Long,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String,
    val distance: Double,
    val following: Boolean,
)