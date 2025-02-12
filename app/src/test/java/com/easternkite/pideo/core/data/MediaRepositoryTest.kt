package com.easternkite.pideo.core.data

import app.cash.turbine.test
import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.network.PideoApi
import com.easternkite.pideo.core.network.model.Meta
import com.easternkite.pideo.core.network.model.video.VideoDocument
import com.easternkite.pideo.core.network.model.video.VideoResponse
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MediaRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: PideoApi
    private lateinit var repository: MediaRepository


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/").toString()
        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PideoApi::class.java)
        repository = DefaultMediaRepository(api)
    }

    @Test
    fun repositoryTest() = runTest {
        val mockJson = VideoResponse(
            documents = listOf(
                VideoDocument(
                    title = "mock",
                    playTime = 100,
                    thumbnail = "mock",
                    url = "mock",
                    datetime = "mock",
                    author = "mock"
                )
            ),
            meta = Meta(
                totalCount = 0,
                pageableCount = 0,
                isEnd = true
            )
        )
        val mockResponse = MockResponse()
            .setBodyDelay(1_000, TimeUnit.MILLISECONDS)
            .setResponseCode(200)
            .setBody(Gson().toJson(mockJson))

        repeat(5) {
            mockWebServer.enqueue(mockResponse)
        }

        repository.getVideoData().test {
            // 첫 로딩
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(mockJson.documents), awaitItem())

            // 페이지 증가
            repository.nextPage()
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(mockJson.documents), awaitItem())

            // 쿼리 변경
            repository.putQuery("123")
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(mockJson.documents), awaitItem())

            // 쿼리 변경2
            repository.putQuery("1234")
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(mockJson.documents), awaitItem())

            // 리스트 갱신
            repository.refresh()
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(mockJson.documents), awaitItem())
        }
    }

}