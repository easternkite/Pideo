package com.easternkite.pideo.core.network

import com.easternkite.pideo.core.network.model.Meta
import com.easternkite.pideo.core.network.model.video.VideoResponse
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PideoApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: PideoApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/").toString()
        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(PideoApi::class.java)
    }

    @Test
    fun kakaoApiCall() {
        runBlocking {
            val mockJson = VideoResponse(
                documents = listOf(),
                meta = Meta(
                    totalCount = 0,
                    pageableCount = 0,
                    isEnd = true
                )
            ).let {
                Gson().toJson(it)
            }

            val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(mockJson)

            mockWebServer.enqueue(mockResponse)

            val response = api.searchVideo(
                query = "개발자",
                sort = "recency",
                page = 1,
                size = 20
            )

            assert(response.isSuccessful)
            assert(response.body() != null)
            assert(response.body()?.documents?.size == 0)
        }
    }
}