package com.data.network.response

data class APIResponse<T>(
    val success: Boolean,
    val message: String,
    val code: Int? = null,
    val data: T? = null
)