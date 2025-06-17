package com.data.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "1")
            .build()
        return chain.proceed(newRequest)
    }
} 