package com.easternkite.pideo.core.network

import com.easternkite.pideo.BuildConfig
import com.easternkite.pideo.core.network.model.image.ImageResponse
import com.easternkite.pideo.core.network.model.video.VideoResponse
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PideoApi {

    @GET("v2/search/vclip")
    suspend fun searchVideo(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<VideoResponse>

    @GET("v2/search/image")
    suspend fun searchImage(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ImageResponse>
}
