package com.easternkite.pideo.core.data.di

import com.easternkite.pideo.core.data.DefaultMediaRepository
import com.easternkite.pideo.core.data.MediaRepository
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
    abstract fun bindMediaRepository(
        repository: DefaultMediaRepository
    ): MediaRepository
}