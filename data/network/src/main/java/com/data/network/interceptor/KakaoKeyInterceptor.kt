package com.data.network.interceptor

import com.data.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KakaoKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val restAPIKey = BuildConfig.KAKAO_REST_API_KEY

        val newRequest = originRequest.newBuilder()
            .header("Authorization", "KakaoAK ${restAPIKey}")
            .build()

        return chain.proceed(newRequest)
    }
}