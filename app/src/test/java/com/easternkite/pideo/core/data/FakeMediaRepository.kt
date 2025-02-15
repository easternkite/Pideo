package com.easternkite.pideo.core.data

import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.network.model.image.ImageDocument
import com.easternkite.pideo.core.network.model.video.VideoDocument
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

class FakeMediaRepository : MediaRepository {
    private val queryFlow = MutableStateFlow("")

    override suspend fun getPictureData(
        sort: String,
        size: Int
    ): Flow<Result<List<ImageDocument>>> {
        return flow {
            val mock = ImageDocument(
                collection = "mock",
                thumbnailUrl = "mock",
                imageUrl = "mock",
                width = 100,
                height = 100,
                displaySiteName = "mock",
                docUrl = "mock",
                datetime = "2017-06-14T00:00:00.000+09:00"
            )
            emit(Result.Loading)
            delay(1000L)
            emit(Result.Success(listOf(mock)))
        }
    }

    override suspend fun getVideoData(
        sort: String,
        size: Int
    ): Flow<Result<List<VideoDocument>>> {
        return flow {
            val mock = VideoDocument(
                title = "mock",
                playTime = 100,
                thumbnail = "mock",
                url = "mock",
                datetime = "2017-06-13T00:00:00.000+09:00",
                author = "mock"
            )
            emit(Result.Loading)
            delay(1000L)
            emit(Result.Success(listOf(mock, mock.copy(datetime = "2025-06-14T00:00:00.000+09:00"))))
        }
    }

    override suspend fun putQuery(query: String) { queryFlow.emit(query) }

    override suspend fun getQuery(): Flow<String> { return queryFlow.filter { it.isNotEmpty() } }

    override suspend fun nextPage() {}

    override suspend fun refresh() {}
}