package com.easternkite.pideo.core.domain

import com.easternkite.pideo.core.data.MediaRepository
import javax.inject.Inject

class PutQueryUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(query: String = "Video") {
        mediaRepository.putQuery(query)
    }
}