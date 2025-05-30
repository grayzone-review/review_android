package com.domain.entity

data class Reviews(
    val currentPage: Int,
    val hasNext: Boolean,
    val reviews: List<Review>
)

data class Review(
    val advantagePoint: String,
    val commentCount: Int,
    val createdAt: String,
    val disadvantagePoint: String,
    val employmentPeriod: String,
    val id: Int,
    val jobRole: String,
    val likeCount: Int,
    val liked: Boolean,
    val managementFeedback: String,
    val ratings: Ratings,
    val title: String
)

data class Ratings(
    val COMPANY_CULTURE: Double,
    val MANAGEMENT: Double,
    val SALARY: Double,
    val WELFARE: Double,
    val WORK_LIFE_BALANCE: Double
)