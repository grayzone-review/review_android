package com.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzUyMDU5NTI4LCJleHAiOjE3NTQ2NTE1Mjh9.Fs5G1uPmVMDOUWzOyarZLPnOnGIBXlzln_94hkIAwY6LBDrW5MgH7PKjst3SL9TA89kZ-if59LKJ7utdlusv0A")
            .build()
        return chain.proceed(newRequest)
    }
} 