package com.data.dto.ResponseModel.search

import com.domain.entity.InteractionCounts

data class UserInteractionResponseDTO(
    val myReviewCount: Int,
    val likeOrCommentReviewCount: Int,
    val followCompanyCount: Int
)

fun UserInteractionResponseDTO.toDomain(): InteractionCounts {
    return InteractionCounts(
        myReviewCount = myReviewCount,
        likeOrCommentReviewCount = likeOrCommentReviewCount,
        followCompanyCount = followCompanyCount
    )
}