package com.data.network.di

import com.data.network.api_service.UpAPIService
import com.data.network.endpoint.UpEndpoint
import com.data.network.interceptor.BearerTokenInterceptor
import com.data.network.interceptor.ErrorInterceptor
import com.data.network.mapper.SearchCompaniesRequestMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpNetworkModule {
    @Provides
    @Singleton
    @Named("Up")
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)      // 응답 수신
            .callTimeout(35, TimeUnit.SECONDS)      // (연결~응답) 전체 호출
            .addInterceptor(BearerTokenInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("Up")
    fun provideRetrofit(
        @Named("Up") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UpEndpoint.Host.baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(
        @Named("Up") retrofit: Retrofit
    ): UpAPIService {
        return retrofit.create(UpAPIService::class.java)
    }

    @Provides
    @Singleton
    @Named("Up")
    fun provideSearchCompaniesRequestMapper(): SearchCompaniesRequestMapper {
        return SearchCompaniesRequestMapper()
    }
}