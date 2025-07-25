package com.data.dto.ResponseModel.search


import com.domain.entity.Ratings
import com.domain.entity.Review
import com.domain.entity.Reviews

data class CompanyReviewsResponseDTO(
    val reviews: List<CompanyReviewDTO>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class CompanyReviewDTO(
    val id: Int,
    val ratings: RatingsResponseDTO,
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
    val liked: Boolean
)

data class RatingsResponseDTO(
    val companyCulture: Double,
    val management: Double,
    val welfare: Double,
    val salary: Double,
    val workLifeBalance: Double
)

fun CompanyReviewsResponseDTO.toDomain(): Reviews = Reviews(
    currentPage = currentPage,
    hasNext = hasNext,
    reviews = reviews.map { it.toDomain() }
)

fun CompanyReviewDTO.toDomain(): Review = Review(
    id = id,
    ratings = ratings.toDomain(),
    author = author,
    title = title,
    advantagePoint = advantagePoint,
    disadvantagePoint = disadvantagePoint,
    managementFeedback = managementFeedback,
    jobRole = jobRole,
    employmentPeriod = employmentPeriod,
    createdAt = createdAt,
    likeCount = likeCount,
    commentCount = commentCount,
    liked = liked
)

private fun RatingsResponseDTO.toDomain(): Ratings = Ratings(
    companyCulture = companyCulture,
    management = management,
    welfare = welfare,
    salary = salary,
    workLifeBalance = workLifeBalance
)
