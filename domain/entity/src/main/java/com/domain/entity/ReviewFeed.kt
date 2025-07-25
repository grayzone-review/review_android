package com.domain.entity


data class ReviewFeeds(
    val reviewFeeds: List<ReviewFeed>
)

data class ReviewFeed(
    val review: Review,
    val compactCompany: CompactCompany
)