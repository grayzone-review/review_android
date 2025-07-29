package com.domain.entity


data class ReviewFeeds(
    val reviewFeeds: List<ReviewFeed>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class ReviewFeed(
    val review: Review,
    val compactCompany: CompactCompany
)