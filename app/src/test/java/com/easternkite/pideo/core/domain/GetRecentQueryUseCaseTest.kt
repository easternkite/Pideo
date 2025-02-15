package com.easternkite.pideo.core.domain

import app.cash.turbine.test
import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.data.FakeMediaRepository
import com.easternkite.pideo.core.data.MediaRepository
import com.easternkite.pideo.core.domain.entity.MediaEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRecentQueryUseCaseTest {
    private lateinit var repository: MediaRepository
    private lateinit var useCase: GetRecentSearchQueryUseCase

    @Before
    fun setUp() {
        repository = FakeMediaRepository()
        useCase = GetRecentSearchQueryUseCase(repository)
    }

    @Test
    fun test() = runTest {
        useCase().test {
            repository.putQuery("Video")
            assertEquals("Video", awaitItem())
            repository.putQuery("Video2")
            assertEquals("Video2", awaitItem())
        }
    }
}
