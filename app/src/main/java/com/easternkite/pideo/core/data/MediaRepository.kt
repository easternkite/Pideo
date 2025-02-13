package com.easternkite.pideo.core.data

import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.network.model.image.ImageDocument
import com.easternkite.pideo.core.network.model.video.VideoDocument
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun getPictureData(
        sort: String = "recency",
        size: Int = 20
    ): Flow<Result<List<ImageDocument>>>

    suspend fun getVideoData(
        sort: String = "recency",
        size: Int = 30
    ): Flow<Result<List<VideoDocument>>>

    suspend fun putQuery(query: String)

    suspend fun nextPage()

    suspend fun refresh()
}
