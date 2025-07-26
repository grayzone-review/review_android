package com.data.repository_implementation

import RequestModel.AuthLoginRequestModel
import RequestModel.LogOutRequestModel
import RequestModel.ReissueRequestModel
import RequestModel.SignUpRequestModel
import RequestModel.VerifyNicknameRequestModel
import com.data.dto.ResponseModel.search.toDomain
import com.data.network.api_service.UpAuthService
import com.domain.entity.Agreement
import com.domain.entity.LogOutResult
import com.domain.entity.LoginResult
import com.domain.entity.ReissueResult
import com.domain.entity.SignUpResult
import com.domain.entity.Terms
import com.domain.entity.VerifyNickNameResult
import com.domain.repository_interface.UpAuthRepository
import com.team.common.feature_api.error.APIException
import com.team.common.feature_api.error.toErrorAction
import javax.inject.Inject

class UpAuthRepositoryImpl @Inject constructor(
    private val upAuthService: UpAuthService
): UpAuthRepository {
    override suspend fun login(
        oAuthToken: String
    ): LoginResult? {
        val requestModel = AuthLoginRequestModel(oauthToken = oAuthToken)
        val responseDTO = upAuthService.login(body = requestModel)
        responseDTO.code?.let { throw APIException(action = it.toErrorAction(), message = responseDTO.message) }
        return responseDTO.data?.toDomain()
    }

    override suspend fun signUp(
        oauthToken: String,
        mainRegionId: Int,
        interestedRegionIds: List<Int>,
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
        responseDTO.code?.let { throw APIException(action = it.toErrorAction(), message = responseDTO.message) }
        return SignUpResult(message = responseDTO.message, success = responseDTO.success)
    }

    override suspend fun verifyNickName(
        nickname: String
    ): VerifyNickNameResult {
        val requestDTO = VerifyNicknameRequestModel(nickname = nickname)
        val responseDTO = upAuthService.verifyNickname(body = requestDTO)
        return VerifyNickNameResult(message = responseDTO.message, success = responseDTO.success)
    }

    override suspend fun terms(
    ): Terms {
        val responseDTO = upAuthService.term()
        return responseDTO.data!!.toDomain()
    }

    override suspend fun logout(
        refreshToken: String
    ): LogOutResult {
        val requestDTO = LogOutRequestModel(refreshToken = refreshToken)
        val responseDTO = upAuthService.logout(body = requestDTO)
        return LogOutResult(success = responseDTO.success, message = responseDTO.message)
    }

    override suspend fun reissueToken(
        refreshToken: String
    ): ReissueResult {
        val requestDTO = ReissueRequestModel(refreshToken = refreshToken)
        val responseDTO = upAuthService.reissueToken(body = requestDTO)
        return ReissueResult(accessToken = responseDTO.data?.accessToken!!, refreshToken = responseDTO.data?.refreshToken!!)
    }

}