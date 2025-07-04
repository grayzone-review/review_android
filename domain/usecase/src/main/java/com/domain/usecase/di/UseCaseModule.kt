package com.domain.usecase.di

import com.domain.usecase.CompanyDetailUseCase
import com.domain.usecase.CompanyDetailUseCaseImpl
import com.domain.usecase.KakaoMapUseCase
import com.domain.usecase.KakaoMapUseCaseImpl
import com.domain.usecase.SearchCompaniesUseCase
import com.domain.usecase.SearchCompaniesUseCaseImpl
import com.domain.usecase.SearchDistrictUseCase
import com.domain.usecase.SearchDistrictUseCaseImpl
import com.domain.usecase.UpAuthUseCase
import com.domain.usecase.UpAuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    
    @Binds
    @Singleton
    abstract fun bindSearchCompaniesUseCase(
        impl: SearchCompaniesUseCaseImpl
    ): SearchCompaniesUseCase

    @Binds
    @Singleton
    abstract fun bindCompanyDetailUseCase(
        impl: CompanyDetailUseCaseImpl
    ): CompanyDetailUseCase

    @Binds
    @Singleton
    abstract fun bindKakaoMapUseCase(
        impl: KakaoMapUseCaseImpl
    ): KakaoMapUseCase

    @Binds
    @Singleton
    abstract fun bindUpAuthUseCase(
        impl: UpAuthUseCaseImpl
    ): UpAuthUseCase

    @Binds
    @Singleton
    abstract fun bindSearchDistrictUseCase(
        impl: SearchDistrictUseCaseImpl
    ): SearchDistrictUseCase
} 