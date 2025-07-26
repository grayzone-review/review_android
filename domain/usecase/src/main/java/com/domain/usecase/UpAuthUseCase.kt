package com.domain.usecase

import com.domain.entity.Agreement
import com.domain.entity.LogOutResult
import com.domain.entity.LoginResult
import com.domain.entity.ReissueResult
import com.domain.entity.SignUpResult
import com.domain.entity.Terms
import com.domain.entity.VerifyNickNameResult
import com.domain.repository_interface.UpAuthRepository
import javax.inject.Inject

interface UpAuthUseCase {
    suspend fun login(oAuthToken: String): LoginResult?
    suspend fun signUp(oauthToken: String, mainRegionId: Int, interestedRegionIds: List<Int>, nickname: String, agreements: List<Agreement>): SignUpResult
    suspend fun verifyNickName(nickname: String): VerifyNickNameResult
    suspend fun terms(): Terms
    suspend fun logout(refreshToken: String): LogOutResult
    suspend fun reissueToken(refreshToken: String): ReissueResult
}

class UpAuthUseCaseImpl @Inject constructor(
    private val upAuthRepository: UpAuthRepository
) : UpAuthUseCase {
    override suspend fun login(oAuthToken: String): LoginResult? {
        return upAuthRepository.login(oAuthToken)
    }

    override suspend fun signUp(oauthToken: String, mainRegionId: Int, interestedRegionIds: List<Int>, nickname: String, agreements: List<Agreement>): SignUpResult {
        return upAuthRepository.signUp(oauthToken = oauthToken, mainRegionId = mainRegionId, interestedRegionIds = interestedRegionIds, nickname = nickname, agreements = agreements)
    }

    override suspend fun verifyNickName(nickname: String): VerifyNickNameResult {
        return upAuthRepository.verifyNickName(nickname)
    }

    override suspend fun terms(): Terms {
        return upAuthRepository.terms()
    }

    override suspend fun logout(refreshToken: String): LogOutResult {
        return upAuthRepository.logout(refreshToken)
    }

    override suspend fun reissueToken(refreshToken: String): ReissueResult {
        return upAuthRepository.reissueToken(refreshToken)
    }
}

