package com.data.dto.ResponseModel.search

import com.domain.entity.Replies
import com.domain.entity.Reply

data class RepliesResponseDTO(
    val replies: List<ReplyDTO>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class ReplyDTO(
    val id: Int,
    val comment: String? = null,
    val authorName: String? = null,
    val createdAt: String? = null,
    val secret: Boolean,
    val visible: Boolean
)

fun RepliesResponseDTO.toDomain(): Replies {
    return Replies(
        replies = replies.map(ReplyDTO::toDomain),
        hasNext = hasNext,
        currentPage = currentPage
    )
}

fun ReplyDTO.toDomain(): Reply {
    return Reply(
        id = id,
        comment = comment,
        authorName = authorName,
        createdAt = createdAt,
        secret = secret,
        visible = visible
    )
}