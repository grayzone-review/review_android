package com.domain.repository_interface

import com.domain.entity.LoginResult
import com.domain.entity.SignUpResult

enum class Agreement { serviceUse, privacy, location }

interface UpAuthRepository {
    suspend fun login(oAuthToken: String): LoginResult
    suspend fun signUp(
        oauthToken: String,
        mainRegionId: Long,
        interestedRegionIds: List<Long>,
        nickname: String,
        agreements: List<Agreement>
    ): SignUpResult
}