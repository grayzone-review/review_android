package com.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzUzMTgwMzQ1LCJleHAiOjE3NTM3ODUxNDV9.CioInbd6DVZe40MrFSeXpo4q6w2eI222a2W4lq6NSNGD5K1JBruzQao5WF6VhQ1rMUUBxZJH9TQJPvZaHhnr9g")
            .build()
        return chain.proceed(newRequest)
    }
} 