package com.domain.entity

data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
