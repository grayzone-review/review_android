package com.data.dto.ResponseModel.search

data class AuthLoginResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
