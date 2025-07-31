package com.domain.entity

data class Replies(
    val replies: List<Reply>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class Reply(
    val id: Int,
    val comment: String? = null,
    val authorName: String? = null,
    val createdAt: String? = null,
    val secret: Boolean,
    val visible: Boolean
)