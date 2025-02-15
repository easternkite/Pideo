package com.easternkite.pideo.core.domain.di

import com.easternkite.pideo.core.data.MediaRepository
import com.easternkite.pideo.core.domain.GetMediaListUseCase
import com.easternkite.pideo.core.domain.PutQueryUseCase
import com.easternkite.pideo.core.domain.entity.RefreshUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetMediaListUseCase(repository: MediaRepository) = GetMediaListUseCase(repository)

    @Provides
    @Singleton
    fun providesPutQueryUseCase(repository: MediaRepository) = PutQueryUseCase(repository)

    @Provides
    @Singleton
    fun providesRefreshUseCase(repository: MediaRepository) = RefreshUseCase(repository)
}