package com.easternkite.pideo.core.data

import com.easternkite.pideo.core.common.Result
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun getPictureData(
        sort: String = "recency",
        size: Int = 20
    ): Flow<Result>

    suspend fun getVideoData(
        sort: String = "recency",
        size: Int = 30
    ): Flow<Result>

    suspend fun putQuery(query: String)

    suspend fun nextPage()

    suspend fun refresh()
}
