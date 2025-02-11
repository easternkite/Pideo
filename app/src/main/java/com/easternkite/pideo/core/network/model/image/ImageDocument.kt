package com.easternkite.pideo.core.network.model.image

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ImageDocument(
    val collection: String,
    val datetime: String,
    @SerializedName("display_sitename") val displaySiteName: String,
    @SerializedName("doc_url") val docUrl: String,
    val height: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    val width: Int
)