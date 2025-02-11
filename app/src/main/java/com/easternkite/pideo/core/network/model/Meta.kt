package com.easternkite.pideo.core.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("total_count") val totalCount: Int
)