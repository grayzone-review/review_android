package com.data.network.di

import com.data.network.APIService
import com.data.network.endpoint.Endpoint
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
object NetworkModule {

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
            .baseUrl(Endpoint.Host.baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchCompaniesRequestMapper(): SearchCompaniesRequestMapper {
        return SearchCompaniesRequestMapper()
    }
}