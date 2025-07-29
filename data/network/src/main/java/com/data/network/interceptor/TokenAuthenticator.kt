package com.data.network.interceptor

import com.data.network.api_service.UpAuthService
import com.data.network.token_refresher.TokenRefresher
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val upAuthService: UpAuthService
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        val bodyString = response.peekBody(Long.MAX_VALUE).string()
        val apiCode = runCatching {
            JSONObject(bodyString).optInt("code")
        }.getOrDefault(0)

        if (apiCode != 3002) return null
        val newAccessToken = runBlocking {
            TokenRefresher.requestRefresh(upAuthService = upAuthService)
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }
}
