package com.data.repository_implementation.di

import com.data.repository_implementation.CompanyDetailRepositoryImpl
import com.data.repository_implementation.SearchCompaniesRepositoryImpl
import com.data.repository_implementation.KakaoMapRepositoryImpl
import com.domain.repository_interface.CompanyDetailRepository
import com.domain.repository_interface.SearchCompaniesRepository
import com.domain.repository_interface.KakaoMapRepository
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
} 