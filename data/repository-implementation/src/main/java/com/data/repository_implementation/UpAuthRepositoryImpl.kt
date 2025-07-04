package com.data.repository_implementation

import RequestModel.AuthLoginRequestModel
import RequestModel.SignUpRequestModel
import RequestModel.VerifyNicknameRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAuthService
import com.domain.entity.LoginResult
import com.domain.entity.SignUpResult
import com.domain.entity.VerifyNickNameResult
import com.domain.repository_interface.Agreement
import com.domain.repository_interface.UpAuthRepository
import javax.inject.Inject

class UpAuthRepositoryImpl @Inject constructor(
    private val upAuthService: UpAuthService
): UpAuthRepository {
    override suspend fun login(
        oAuthToken: String
    ): LoginResult {
        val requestModel = AuthLoginRequestModel(oauthToken = oAuthToken)
        val responseDTO = upAuthService.login(body = requestModel)
        return responseDTO.data?.toDomain()!!
    }

    override suspend fun signUp(
        oauthToken: String,
        mainRegionId: Long,
        interestedRegionIds: List<Long>,
        nickname: String,
        agreements: List<Agreement>
    ): SignUpResult {
        val requestDTO = SignUpRequestModel(
            oauthToken = oauthToken,
            mainRegionId = mainRegionId,
            interestedRegionIds = interestedRegionIds,
            nickname = nickname,
            agreements = agreements.map { it.name }
        )
        val responseDTO = upAuthService.signUp(requestDTO)
        return SignUpResult(message = responseDTO.message, success = responseDTO.success)
    }

    override suspend fun verifyNickName(
        nickname: String
    ): VerifyNickNameResult {
        val requestDTO = VerifyNicknameRequestModel(nickname = nickname)
        val responseDTO = upAuthService.verifyNickname(body = requestDTO)
        return VerifyNickNameResult(message = responseDTO.message, success = responseDTO.success)
    }
}