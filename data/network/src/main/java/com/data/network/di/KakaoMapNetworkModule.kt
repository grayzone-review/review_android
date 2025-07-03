package com.data.network.di

import com.data.network.api_service.KakaoMapAPIService
import com.data.network.endpoint.KakaoMapEndpoint
import com.data.network.interceptor.ErrorInterceptor
import com.data.network.interceptor.KakaoKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoMapNetworkModule {

    @Provides
    @Singleton
    @Named("KakaoOkHttp")
    fun provideKakaoOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(KakaoKeyInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()
    }

    /* ───── Kakao Retrofit ───── */
    @Provides
    @Singleton
    @Named("KakaoRetrofit")
    fun provideKakaoRetrofit(
        @Named("KakaoOkHttp")client:OkHttpClient
    ):Retrofit =
        Retrofit.Builder()
            .baseUrl(KakaoMapEndpoint.Host.baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /* ───── Kakao 서비스 ───── */
    @Provides
    @Singleton
    @Named("KakaoAPI")
    fun provideKakaoService(
        @Named("KakaoRetrofit")retrofit:Retrofit
    ): KakaoMapAPIService =
        retrofit.create(KakaoMapAPIService::class.java)
}