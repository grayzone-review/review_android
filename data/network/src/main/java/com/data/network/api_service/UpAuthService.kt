package com.data.network.api_service

import RequestModel.AuthLoginRequestModel
import RequestModel.NicknameVerifyRequestModel
import RequestModel.SignUpRequestModel
import com.data.dto.ResponseModel.search.AuthLoginResponseDTO
import com.data.network.endpoint.UpEndpoint
import com.data.network.response.APIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UpAuthService {
    @POST(UpEndpoint.Path.AUTH_LOGIN)
    suspend fun login(
        @Body body: AuthLoginRequestModel
    ): APIResponse<AuthLoginResponseDTO>

    @POST(UpEndpoint.Path.AUTH_SIGNUP)
    suspend fun signUp(
        @Body body: SignUpRequestModel
    ): APIResponse<Unit>

    @POST(UpEndpoint.Path.NICKNAME_VERIFY)
    suspend fun verifyNickname(
        @Body body: NicknameVerifyRequestModel
    ): APIResponse<Unit>

}