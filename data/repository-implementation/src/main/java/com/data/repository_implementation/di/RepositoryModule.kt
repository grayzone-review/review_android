package com.data.repository_implementation.di

import com.data.repository_implementation.CompanyDetailRepositoryImpl
import com.data.repository_implementation.KakaoMapRepositoryImpl
import com.data.repository_implementation.ReviewRepositoryImpl
import com.data.repository_implementation.SearchCompaniesRepositoryImpl
import com.data.repository_implementation.SearchDistrictRepositoryImpl
import com.data.repository_implementation.UpAuthRepositoryImpl
import com.data.repository_implementation.UserRepositoryImpl
import com.domain.repository_interface.CompanyDetailRepository
import com.domain.repository_interface.KakaoMapRepository
import com.domain.repository_interface.ReviewRepository
import com.domain.repository_interface.SearchCompaniesRepository
import com.domain.repository_interface.SearchDistrictRepository
import com.domain.repository_interface.UpAuthRepository
import com.domain.repository_interface.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindSearchCompaniesRepository(
        impl: SearchCompaniesRepositoryImpl
    ): SearchCompaniesRepository

    @Binds
    @Singleton
    abstract fun bindCompanyDetailRepository(
        impl: CompanyDetailRepositoryImpl
    ): CompanyDetailRepository

    @Binds
    @Singleton
    abstract fun bindKakaoMapRepository(
        impl: KakaoMapRepositoryImpl
    ): KakaoMapRepository

    @Binds
    @Singleton
    abstract fun bindUpAuthRepository(
        impl: UpAuthRepositoryImpl
    ): UpAuthRepository

    @Binds
    @Singleton
    abstract fun bindSearchDistrictRepository(
        impl: SearchDistrictRepositoryImpl
    ): SearchDistrictRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        impl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
} 