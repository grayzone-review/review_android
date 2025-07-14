package com.domain.entity

data class Reviews(
    val currentPage: Int,
    val hasNext: Boolean,
    val reviews: List<Review>
)

data class Review(
    val nickName: String,           // 추가요청 해두었음
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
    val totalRating: Double,            // DTO 매핑 시 계산하여 변환
    val ratings: Ratings,
    val title: String
)

data class Ratings(
    val COMPANY_CULTURE: Double = 0.0,
    val MANAGEMENT: Double = 0.0,
    val SALARY: Double = 0.0,
    val WELFARE: Double = 0.0,
    val WORK_LIFE_BALANCE: Double = 0.0
)