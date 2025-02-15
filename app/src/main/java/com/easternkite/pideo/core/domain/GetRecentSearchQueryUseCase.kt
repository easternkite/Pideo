package com.easternkite.pideo.core.domain

import com.easternkite.pideo.core.data.MediaRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentSearchQueryUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke() = flow {
        emitAll(mediaRepository.getQuery())
    }
}