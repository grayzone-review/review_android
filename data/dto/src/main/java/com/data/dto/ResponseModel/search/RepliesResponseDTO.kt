package com.data.dto.ResponseModel.search

import com.domain.entity.Replies
import com.domain.entity.Reply

data class RepliesResponseDTO(
    val replies: List<ReplyDto>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class ReplyDto(
    val id: Int,
    val comment: String? = null,
    val authorName: String? = null,
    val createdAt: String? = null,
    val secret: Boolean,
    val visible: Boolean
)

fun RepliesResponseDTO.toDomain(): Replies {
    return Replies(
        replies = replies.map(ReplyDto::toDomain),
        hasNext = hasNext,
        currentPage = currentPage
    )
}

fun ReplyDto.toDomain(): Reply {
    return Reply(
        id = id,
        comment = comment,
        authorName = authorName,
        createdAt = createdAt,
        secret = secret,
        visible = visible
    )
}