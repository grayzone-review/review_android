package com.presentation.archive.di

import com.presentation.archive.ArchiveAPI
import com.presentation.archive.ArchiveAPIImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UIModule {
    @Provides
    fun provideArchiveAPI(): ArchiveAPI {
        return ArchiveAPIImpl()
    }
}