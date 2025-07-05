package com.domain.usecase

import com.domain.entity.LoginResult
import com.domain.entity.SignUpResult
import com.domain.entity.Terms
import com.domain.entity.VerifyNickNameResult
import com.domain.repository_interface.Agreement
import com.domain.repository_interface.UpAuthRepository
import javax.inject.Inject

interface UpAuthUseCase {
    suspend fun login(oAuthToken: String): LoginResult
    suspend fun signUp(
        oauthToken: String,
        mainRegionId: Long,
        interestedRegionIds: List<Long>,
        nickname: String,
        agreements: List<Agreement>
    ): SignUpResult
    suspend fun verifyNickName(nickname: String): VerifyNickNameResult
    suspend fun terms(): Terms
}

class UpAuthUseCaseImpl @Inject constructor(
    private val upAuthRepository: UpAuthRepository
) : UpAuthUseCase {
    override suspend fun login(oAuthToken: String): LoginResult {
        return upAuthRepository.login(oAuthToken)
    }

    override suspend fun signUp(
        oauthToken: String,
        mainRegionId: Long,
        interestedRegionIds: List<Long>,
        nickname: String,
        agreements: List<Agreement>
    ): SignUpResult {
        return upAuthRepository.signUp(
            oauthToken = oauthToken,
            mainRegionId = mainRegionId,
            interestedRegionIds = interestedRegionIds,
            nickname = nickname,
            agreements = agreements
        )
    }

    override suspend fun verifyNickName(nickname: String): VerifyNickNameResult {
        return upAuthRepository.verifyNickName(nickname)
    }

    override suspend fun terms(): Terms {
        return upAuthRepository.terms()
    }
}

