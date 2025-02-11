package com.easternkite.pideo.core.network.model.video

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VideoDocument(
    val author: String,
    val datetime: String,
    @SerializedName("play_time") val playTime: Int,
    val thumbnail: String,
    val title: String,
    val url: String
)