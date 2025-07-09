package com.data.network.di
import com.data.network.api_service.UpAuthService
import com.data.network.endpoint.UpEndpoint
import com.data.network.interceptor.ErrorInterceptor
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
object UpAuthNetworkModule {
    @Provides
    @Singleton
    @Named("UpAuth")
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ErrorInterceptor())
            .build()

    @Provides
    @Singleton
    @Named("UpAuth")
    fun provideRetrofit(
        @Named("UpAuth") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(UpEndpoint.Host.baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUpAuthService(
        @Named("UpAuth") retrofit: Retrofit
    ): UpAuthService =
        retrofit.create(UpAuthService::class.java)
}
