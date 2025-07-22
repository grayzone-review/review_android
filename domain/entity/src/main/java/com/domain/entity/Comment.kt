package com.domain.entity

data class Comments(
    val comments: List<Comment>,
    val hasNext: Boolean,
    val currentPage: Int
)

data class Comment(
    val id: Int,
    val comment: String,
    val authorName: String,
    val createdAt: String,
    val replyCount: Int,
    val secret: Boolean,
    val visible: Boolean
)