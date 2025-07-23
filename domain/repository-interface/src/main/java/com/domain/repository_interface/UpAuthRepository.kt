package com.domain.repository_interface

import com.domain.entity.Agreement
import com.domain.entity.LoginResult
import com.domain.entity.SignUpResult
import com.domain.entity.Terms
import com.domain.entity.VerifyNickNameResult

interface UpAuthRepository {
    suspend fun login(oAuthToken: String): LoginResult
    suspend fun signUp(
        oauthToken: String,
        mainRegionId: Int,
        interestedRegionIds: List<Int>,
        nickname: String,
        agreements: List<Agreement>
    ): SignUpResult
    suspend fun verifyNickName(
        nickname: String
    ): VerifyNickNameResult
    suspend fun terms(
    ): Terms
}