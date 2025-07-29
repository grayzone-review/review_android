package com.data.network.token_refresher

import RequestModel.ReissueRequestModel
import com.data.network.api_service.UpAuthService
import com.domain.entity.LoginResult
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import token_storage.TokenStoreService

object TokenRefresher {
    private val mutex = Mutex()

    suspend fun requestRefresh(upAuthService: UpAuthService): String {
        return mutex.withLock {
            val requestDTO = ReissueRequestModel(refreshToken = TokenStoreService.refreshToken())
            val responseDTO = upAuthService.reissueToken(body = requestDTO)
            val data = responseDTO.data ?: throw IllegalStateException("ERROR: 토큰 재발급 실패 failed")

            val loginResult = LoginResult(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                expiresIn = 0
            )
            TokenStoreService.save(loginResult)
            return data.accessToken
        }
    }
}