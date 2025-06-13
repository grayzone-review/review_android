package com.domain.usecase.di

import com.domain.usecase.SearchCompaniesUseCase
import com.domain.usecase.SearchCompaniesUseCaseImpl
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
} 