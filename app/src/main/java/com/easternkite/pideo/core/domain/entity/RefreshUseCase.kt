package com.easternkite.pideo.core.domain.entity

import com.easternkite.pideo.core.data.MediaRepository
import javax.inject.Inject

class RefreshUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke() {
        repository.refresh()
    }
}