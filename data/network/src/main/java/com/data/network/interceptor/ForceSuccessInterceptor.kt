package com.data.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ForceSuccessInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request  = chain.request()
        val original = chain.proceed(request)

        // 200 ~ 299 는 원본 성공 응답
        if (original.isSuccessful) return original
        Log.w(
            "ErrorInterceptor",
            """
            ⬇︎ HTTP ERROR
            • URL     : ${request.method} ${request.url}
            • Code    : ${original.code}
            • Headers : ${original.headers}
            • Body    : ${original.peekBody(Long.MAX_VALUE).string().take(500)}
            """.trimIndent()
        )

        val newBody = original.peekBody(Long.MAX_VALUE)
        return original.newBuilder()
            .code(200)
            .body(newBody)
            .build()
    }
}