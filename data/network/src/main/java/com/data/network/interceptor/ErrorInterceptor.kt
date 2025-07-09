package com.data.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    override fun intercept(chain:Interceptor.Chain):Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            Log.w("ErrorInterceptor","HTTP ${response.code}")
            // peekBody 로 에러 바디 로깅 정도만
        }
        return response      // 예외 X
    }
}