package com.easternkite.pideo.core.data

import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.network.PideoApi
import com.easternkite.pideo.core.network.model.image.ImageDocument
import com.easternkite.pideo.core.network.model.video.VideoDocument
import com.easternkite.pideo.core.network.onFailure
import com.easternkite.pideo.core.network.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultMediaRepository @Inject constructor(
    private val api: PideoApi,
) : MediaRepository {
    private val pageFlow = MutableStateFlow(1)
    private val queryFlow = MutableStateFlow("")
    private val refreshFlow = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    private val combinedFlow = combine(
        pageFlow,
        queryFlow,
        refreshFlow,
        transform = { page, query, result -> Triple(page, query, result) }
    ).debounce(300L)

    override suspend fun getPictureData(sort: String, size: Int): Flow<Result<List<ImageDocument>>> {
        return combinedFlow.flatMapLatest { (page, query) ->
            flow {
                emit(Result.Loading)
                val result = runCatching {
                    api.searchImage(
                        query = query,
                        sort = sort,
                        page = page,
                        size = size
                    )
                }.onFailure {
                    emit(Result.Error(it))
                }.getOrNull()

                result?.onSuccess {
                    emit(Result.Success(it.documents))
                }?.onFailure {
                    emit(Result.Error(it))
                }
            }
        }
    }

    override suspend fun getVideoData(sort: String, size: Int): Flow<Result<List<VideoDocument>>> {
        return combinedFlow.flatMapLatest { (page, query) ->
            flow {
                emit(Result.Loading)
                val result = runCatching {
                    api.searchVideo(
                        query = query,
                        sort = sort,
                        page = page,
                        size = size
                    )
                }.onFailure {
                    emit(Result.Error(it))
                }.getOrNull()

                result?.onSuccess {
                    emit(Result.Success(it.documents))
                }?.onFailure {
                    emit(Result.Error(it))
                }
            }
        }
    }

    override suspend fun putQuery(query: String) {
        queryFlow.update { query }
    }

    override suspend fun getQuery(): Flow<String> {
        return flow {
            emitAll(queryFlow)
        }
    }

    override suspend fun nextPage() {
        pageFlow.emit(pageFlow.value + 1)
    }

    override suspend fun refresh() {
        refreshFlow.emit(!refreshFlow.value)
    }
}