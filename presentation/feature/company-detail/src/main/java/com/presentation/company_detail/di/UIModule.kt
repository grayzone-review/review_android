package com.presentation.company_detail.di

import com.presentation.company_detail.CompanyDetailAPI
import com.presentation.company_detail.CompanyDetailAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object UIModule {

    @Provides
    fun provideCompanyDetailAPI(): CompanyDetailAPI {
        return CompanyDetailAPIImpl()
    }

}