package com.data.network.response

data class APIMessageResponse(
    val success: Boolean,
    val message: String,
)

fun APIMessageResponse.throwIfFailed() {
    if (!success) throw IllegalStateException(message)
}