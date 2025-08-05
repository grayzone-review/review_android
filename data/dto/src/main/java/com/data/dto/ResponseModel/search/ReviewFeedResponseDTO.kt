package com.data.dto.ResponseModel.search

import com.domain.entity.CompactCompany
import com.domain.entity.Ratings
import com.domain.entity.Review
import com.domain.entity.ReviewFeed
import com.domain.entity.ReviewFeeds

data class ReviewFeedResponseDTO(
    val reviews: List<ReviewFeedItemDTO>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class ReviewFeedItemDTO(
    val companyReview: ReviewFeedCompanyReviewDTO,
    val company: ReviewFeedCompactCompanyDTO,
)

data class ReviewFeedCompanyReviewDTO(
    val id: Long,
    val ratings: ReviewFeedRatingsDTO,
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

data class ReviewFeedRatingsDTO(
    val companyCulture: Double,
    val management: Double,
    val welfare: Double,
    val salary: Double,
    val workLifeBalance: Double,
)

data class ReviewFeedCompactCompanyDTO(
    val id: Int,
    val companyName: String,
    val companyAddress: String,
    val totalRating: Double,
    val reviewTitle: String,
    val distance: Double,
    val following: Boolean,
)

fun ReviewFeedResponseDTO.toDomain(): ReviewFeeds {
    return ReviewFeeds(
        reviewFeeds = reviews.map { it.toDomain() } ,
        hasNext = hasNext,
        currentPage = currentPage
    )
}

private fun ReviewFeedItemDTO.toDomain(): ReviewFeed {
    return ReviewFeed(
        review = companyReview.toDomain(),
        compactCompany = company.toDomain()
    )
}

private fun ReviewFeedCompanyReviewDTO.toDomain(): Review {
    return Review(
        id = id.toInt(),
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
}

private fun ReviewFeedRatingsDTO.toDomain(): Ratings {
    return Ratings(
        companyCulture = companyCulture,
        management = management,
        salary = salary,
        welfare = welfare,
        workLifeBalance = workLifeBalance
    )
}

private fun ReviewFeedCompactCompanyDTO.toDomain(): CompactCompany {
    return CompactCompany(
        id = id,
        companyName = companyName,
        companyAddress = companyAddress,
        totalRating = totalRating,
        reviewTitle = reviewTitle,
        distance = distance,
        following = following
    )
}