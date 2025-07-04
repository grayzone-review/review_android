package com.data.dto.ResponseModel.search

import com.domain.entity.LoginResult

data class AuthLoginResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

fun AuthLoginResponseDTO.toDomain(): LoginResult {
    return LoginResult(accessToken, refreshToken, expiresIn)
}
