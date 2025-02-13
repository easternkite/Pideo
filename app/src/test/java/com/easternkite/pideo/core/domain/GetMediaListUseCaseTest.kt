package com.easternkite.pideo.core.domain

import app.cash.turbine.test
import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.data.FakeMediaRepository
import com.easternkite.pideo.core.data.MediaRepository
import com.easternkite.pideo.core.domain.entity.MediaEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMediaListUseCaseTest {
    private lateinit var repository: MediaRepository
    private lateinit var useCase: GetMediaListUseCase

    @Before
    fun setUp() {
        repository = FakeMediaRepository()
        useCase = GetMediaListUseCase(repository)
    }

    @Test
    fun test() = runTest {
        val expected = listOf(
            MediaEntity(
                name = "mock",
                imageUrl = "mock",
                dateTime = 1749826800000
            ),
            MediaEntity(
                name = "mock",
                imageUrl = "mock",
                dateTime = 1497366000000
            ),
            MediaEntity(
                name = "mock",
                imageUrl = "mock",
                dateTime = 1497279600000
            )
        )

        useCase().test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(expected), awaitItem())
            awaitComplete()
        }
    }
}
