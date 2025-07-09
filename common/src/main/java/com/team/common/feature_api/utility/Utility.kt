package com.team.common.feature_api.utility

import androidx.compose.ui.graphics.Color
import kotlin.math.floor

object Utility {
    /**
     * 총점(0‥5)을 받아 5점 만점의
     * (가득, 반, 빈) 별 개수를 반환합니다.
     *
     * ex) 3.7 → StarCounts(3, 1, 1)
     */
    data class StarCounts(val full: Int, val half: Int, val empty: Int)
    fun calculateStarCounts(totalScore: Double): StarCounts {
        val floored    = floor(totalScore * 2.0) / 2.0
        val full       = floored.toInt()
        val half       = if (floored - full >= 0.5) 1 else 0
        val empty      = 5 - full - half
        return StarCounts(full, half, empty)
    }

    fun hexToColor(hex: String): Color {
        val cleaned = hex.removePrefix("#")
        val argb = when (cleaned.length) {
            3 -> buildString {          // #RGB → #FFRRGGBB
                append("FF")
                cleaned.forEach { append("$it$it") }
            }
            6 -> "FF$cleaned"           // #RRGGBB → #FFRRGGBB
            8 -> cleaned                // #AARRGGBB
            else -> throw IllegalArgumentException("Invalid hex color: $hex")
        }.toLong(16).toInt()

        return Color(argb)
    }

}