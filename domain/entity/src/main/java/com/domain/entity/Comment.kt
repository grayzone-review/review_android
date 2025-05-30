package com.domain.entity

data class Comments(
    val comments: List<Comment>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class Comment(
    val id: Long,
    val comment: String,
    val authorName: String,
    val createdAt: String,
    val replyCount: Int,
    val secret: Boolean,
    val visible: Boolean
)