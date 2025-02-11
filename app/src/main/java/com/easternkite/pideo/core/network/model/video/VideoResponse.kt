package com.easternkite.pideo.core.network.model.video

import androidx.annotation.Keep
import com.easternkite.pideo.core.network.model.Meta

@Keep
data class VideoResponse(
    val documents: List<VideoDocument>,
    val meta: Meta
)