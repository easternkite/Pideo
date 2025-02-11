package com.easternkite.pideo.core.network

import com.easternkite.pideo.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class PideoHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .header("Authorization", "KakaoAK ${BuildConfig.API_KEY}")
            .build()

        return chain.proceed(request)
    }
}