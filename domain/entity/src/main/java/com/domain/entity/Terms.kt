package com.domain.entity

data class Terms(
    val terms: List<TermInfo>,
)

data class TermInfo(
    val term: String,
    val url: String,
    val code: String,
    val required: Boolean,
)