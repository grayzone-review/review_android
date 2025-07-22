package com.domain.entity

import kotlin.math.roundToInt

data class Reviews(
    val currentPage: Int? = null,
    val hasNext: Boolean? = null,
    val reviews: List<Review>? = null
)

data class Review(
    val id: Int? = null,
    val ratings: Ratings? = null,
    val author: String? = null,
    val title: String? = null,
    val advantagePoint: String? = null,
    val disadvantagePoint: String? = null,
    val managementFeedback: String? = null,
    val jobRole: String? = null,
    val employmentPeriod: String? = null,
    val createdAt: String? = null,
    val likeCount: Int? = null,
    val commentCount: Int? = null,
    val liked: Boolean? = null
)

data class Ratings(
    val companyCulture: Double? = null,
    val management: Double? = null,
    val salary: Double? = null,
    val welfare: Double? = null,
    val workLifeBalance: Double? = null
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