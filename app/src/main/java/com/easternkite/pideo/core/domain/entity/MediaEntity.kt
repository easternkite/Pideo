package com.easternkite.pideo.core.domain.entity

import com.easternkite.pideo.core.network.model.image.ImageDocument
import com.easternkite.pideo.core.network.model.video.VideoDocument
import java.text.SimpleDateFormat
import java.util.Locale

data class MediaEntity(
    val name: String = "",
    val imageUrl: String = "",
    val dateTime: Long = 0L
)

fun ImageDocument.toMediaEntity() = MediaEntity(
    name = displaySiteName,
    imageUrl = thumbnailUrl,
    dateTime = datetime.toDateTime()
)

fun VideoDocument.toMediaEntity() = MediaEntity(
    name = title,
    imageUrl = thumbnail,
    dateTime = datetime.toDateTime()
)

fun String.toDateTime(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.KOREA)
    return format.parse(this)?.time ?: 0L
}
