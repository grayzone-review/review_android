package com.domain.entity

data class Replies(
    val replies: List<Reply>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class Reply(
    val id: Long,
    val comment: String,
    val authorName: String,
    val createdAt: String,
    val secret: Boolean,
    val visible: Boolean
)