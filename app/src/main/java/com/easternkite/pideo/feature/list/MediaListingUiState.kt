package com.easternkite.pideo.feature.list

import com.easternkite.pideo.core.domain.entity.MediaEntity


data class MediaListingUiState(
    val query: String = "Video",
    val isLoading: Boolean = false,
    val mediaList: List<MediaEntity> = emptyList(),
    val error: String? = null
)
