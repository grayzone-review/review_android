package com.domain.entity

import kotlin.math.roundToInt

data class Reviews(
    val currentPage: Int,
    val hasNext: Boolean,
    val reviews: List<Review>
)

data class Review(
    val id: Int,
    val ratings: Ratings,
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

data class Ratings(
    val companyCulture: Double = 0.0,
    val management: Double = 0.0,
    val salary: Double = 0.0,
    val welfare: Double = 0.0,
    val workLifeBalance: Double = 0.0
)

fun Ratings.roundedAverage(): Double {
    val raw = (companyCulture + management + salary + welfare + workLifeBalance) / 5
    return (raw * 10).roundToInt() / 10.0
}