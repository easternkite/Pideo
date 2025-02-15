package com.easternkite.pideo.feature.list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.easternkite.pideo.feature.list.MediaListingScreen
import kotlinx.serialization.Serializable

@Serializable
object MediaListingRoute

fun NavGraphBuilder.mediaListingRoute() {
    composable<MediaListingRoute> {
        MediaListingScreen()
    }
}