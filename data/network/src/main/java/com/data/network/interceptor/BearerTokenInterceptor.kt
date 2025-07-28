package com.data.network.interceptor

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import token_storage.TokenStoreService

class BearerTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = runBlocking { TokenStoreService.accessToken() }
        Log.w("Token Info:", accessToken)
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(newRequest)
    }
} 