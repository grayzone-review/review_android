package com.data.dto.ResponseModel.search

import com.domain.entity.MyArchiveReview
import com.domain.entity.MyArchiveReviews

data class MyArchiveReviewsResponseDTO(
    val reviews: List<MyArchiveReviewDTO> = emptyList(),
    val hasNext: Boolean = false,
    val currentPage: Int = 0
)

data class MyArchiveReviewDTO(
    val id: Int,
    val totalRating: Double,
    val title: String,
    val companyId: Int,
    val companyName: String,
    val companyAddress: String,
    val jobRole: String,
    val createdAt: String,
    val likeCount: Int,
    val commentCount: Int
)

fun MyArchiveReviewsResponseDTO.toDomain(): MyArchiveReviews =
    MyArchiveReviews(
        reviews = reviews.map(MyArchiveReviewDTO::toDomain),
        hasNext = hasNext,
        currentPage = currentPage
    )

private fun MyArchiveReviewDTO.toDomain(): MyArchiveReview =
    MyArchiveReview(
        id = id,
        totalRating = totalRating,
        title = title,
        companyId = companyId,
        companyName = companyName,
        companyAddress = companyAddress,
        jobRole = jobRole,
        createdAt = createdAt,
        likeCount = likeCount,
        commentCount = commentCount
    )
