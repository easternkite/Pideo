package com.easternkite.pideo.core.network.model.image

import androidx.annotation.Keep
import com.easternkite.pideo.core.network.model.Meta

@Keep
data class ImageResponse(
    val documents: List<ImageDocument>,
    val meta: Meta
)