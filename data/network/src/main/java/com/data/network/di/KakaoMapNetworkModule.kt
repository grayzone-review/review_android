package com.data.network.di

import com.data.network.api_service.KakaoMapAPIService
import com.data.network.endpoint.KakaoMapEndpoint
import com.data.network.interceptor.ErrorInterceptor
import com.data.network.interceptor.KakaoKeyInterceptor
import com.data.network.mapper.KakaoRequestMapper
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
    @Named("KakaoMap")
    fun provideKakaoOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(KakaoKeyInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("KakaoMap")
    fun provideKakaoRetrofit(
        @Named("KakaoMap") client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KakaoMapEndpoint.Host.baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoService(
        @Named("KakaoMap") retrofit: Retrofit
    ): KakaoMapAPIService {
        return retrofit.create(KakaoMapAPIService::class.java)
    }

    @Provides
    @Singleton
    @Named("KakaoMap")
    fun provideKakaoMapRequestMapper(): KakaoRequestMapper {
        return KakaoRequestMapper()
    }
}