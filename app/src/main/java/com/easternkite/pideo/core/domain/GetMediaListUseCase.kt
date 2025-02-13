package com.easternkite.pideo.core.domain

import com.easternkite.pideo.core.common.Result
import com.easternkite.pideo.core.common.dataOrNull
import com.easternkite.pideo.core.data.MediaRepository
import com.easternkite.pideo.core.domain.entity.MediaEntity
import com.easternkite.pideo.core.domain.entity.toMediaEntity
import com.easternkite.pideo.core.network.model.image.ImageDocument
import com.easternkite.pideo.core.network.model.video.VideoDocument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetMediaListUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() = flow {
        val result = combine(
            mediaRepository.getPictureData(),
            mediaRepository.getVideoData(),
            transform = { picture, video -> picture to video }
        ).mapLatest { (picture, video) ->
            val pictureData = picture.dataOrNull?.map(ImageDocument::toMediaEntity) ?: emptyList()
            val videoData = video.dataOrNull?.map(VideoDocument::toMediaEntity) ?: emptyList()


            if (picture is Result.Error || video is Result.Error) {
                val pictureError = (picture as? Result.Error)?.exception
                val videoError = (video as? Result.Error)?.exception
                return@mapLatest Result.Error(pictureError ?: videoError ?: Exception("Unknown Error"))
            }

            if (picture is Result.Loading || video is Result.Loading) {
                return@mapLatest Result.Loading
            }

            val sorted = (pictureData + videoData)
                .sortedByDescending(MediaEntity::dateTime)

            Result.Success(sorted)
        }
        emitAll(result)
    }
}