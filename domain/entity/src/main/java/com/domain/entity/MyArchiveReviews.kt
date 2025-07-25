package com.domain.entity

data class MyArchiveReviews(
    val reviews: List<MyArchiveReview> = emptyList(),
    val hasNext: Boolean = false,
    val currentPage: Int = 0
)

data class MyArchiveReview(
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
