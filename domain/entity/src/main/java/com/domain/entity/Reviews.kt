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

/**
 * 모든 항목이 null‑safe 하도록 평균 계산.
 *  - null 값은 0.0으로 간주해 5개 항목 평균
 *  - 소수 첫째 자리까지 반올림
 */
fun Ratings.roundedAverage(): Double {
    val raw = (
            (companyCulture ?: 0.0) +
                    (management ?: 0.0) +
                    (salary ?: 0.0) +
                    (welfare ?: 0.0) +
                    (workLifeBalance ?: 0.0)
            ) / 5
    return (raw * 10).roundToInt() / 10.0
}