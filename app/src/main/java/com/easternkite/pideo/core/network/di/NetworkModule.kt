package com.easternkite.pideo.core.network.di

import com.easternkite.pideo.core.network.PideoApi
import com.easternkite.pideo.core.network.PideoHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providePideoApi(): PideoApi {
        val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(PideoHeaderInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PideoApi::class.java)
    }
}