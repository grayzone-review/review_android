package com.data.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            Log.d("ErrorInterceptor", "인터셉터 시작")
            val request = chain.request()
            Log.d("ErrorInterceptor", "요청 URL: ${request.url}")
            
            val response = chain.proceed(request)
            Log.d("ErrorInterceptor", "응답 코드: ${response.code}")
            
            when (response.code) {
                in 200..299 -> return response
                401 -> throw IOException("인증에 실패했습니다.")
                403 -> throw IOException("접근이 거부되었습니다.")
                404 -> throw IOException("요청한 리소스를 찾을 수 없습니다.")
                500 -> throw IOException("서버 오류가 발생했습니다.")
                else -> throw IOException("알 수 없는 오류가 발생했습니다. (${response.code})")
            }
        } catch (e: Exception) {
            Log.e("ErrorInterceptor", "네트워크 요청 실패", e)
            throw e
        }
    }
} 