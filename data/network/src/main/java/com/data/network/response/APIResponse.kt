package com.data.network.response

data class APIResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
