package com.data.network.response

data class APIResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)

inline fun <T, R> APIResponse<T>.mapOrThrow(
    transform: (T) -> R
): R {
    if (!success) throw IllegalStateException(message)
    return transform(data)
}