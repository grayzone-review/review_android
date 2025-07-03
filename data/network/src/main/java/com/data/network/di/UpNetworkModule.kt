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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(BearerTokenInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UpEndpoint.Host.upBaseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): UpAPIService {
        return retrofit.create(UpAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchCompaniesRequestMapper(): SearchCompaniesRequestMapper {
        return SearchCompaniesRequestMapper()
    }
}