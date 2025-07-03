package com.data.network.interceptor

import com.data.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KakaoKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val nativeKey = BuildConfig.KAKAO_NATIVE_APP_KEY

        val newRequest = originRequest.newBuilder()
            .header("Authorization", "KakaoAK${ nativeKey }")
            .build()

        return chain.proceed(newRequest)
    }
}