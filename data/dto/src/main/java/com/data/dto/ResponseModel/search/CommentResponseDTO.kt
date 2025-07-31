package com.data.dto.ResponseModel.search

import com.domain.entity.Comment
import com.domain.entity.Comments

data class CommentsDTO(
    val comments: List<CommentDto>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class CommentDto(
    val id: Int,
    val comment: String? = null,
    val authorName: String? = null,
    val createdAt: String? = null,
    val replyCount: Int,
    val secret: Boolean,
    val visible: Boolean
)

/* ---------- DTO → Domain ---------- */

fun CommentsDTO.toDomain(): Comments =
    Comments(
        comments = comments.map(CommentDto::toDomain),
        hasNext = hasNext,
        currentPage = currentPage
    )

fun CommentDto.toDomain(): Comment =
    Comment(
        id = id,
        comment = comment,
        authorName = authorName,
        createdAt = createdAt,
        replyCount = replyCount,
        secret = secret,
        visible = visible
    )
