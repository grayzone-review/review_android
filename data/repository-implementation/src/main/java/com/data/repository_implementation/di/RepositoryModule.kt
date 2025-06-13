package com.data.repository_implementation.di

import com.data.repository_implementation.SearchCompaniesRepositoryImpl
import com.domain.repository.SearchCompaniesRepository
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
} 