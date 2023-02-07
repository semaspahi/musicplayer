package com.sema.spotififi.ui.navigation

import com.sema.domain.model.PopularArtistsResource
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object PopularArtists : Screen("PopularArtistsScreen")

    object ArtistDetailScreen :
        Screen("ArtistDetailScreen/{permalink}/{count}/{artistName}?encodedUrlString={encodedImageUrlString}") {
        const val NAV_ARG_ARTIST_NAME = "artistName"
        const val NAV_ARG_TRACK_COUNT = "count"
        const val NAV_ARG_ENCODED_IMAGE_URL_STRING = "encodedImageUrlString"
        const val NAV_ARG_PERMALINK_STRING = "permalink"

        fun buildRoute(artist: PopularArtistsResource): String {
            val encodedImageUrl = URLEncoder.encode(
                artist.imageUrl,
                StandardCharsets.UTF_8.toString()
            )
            val url =
                "ArtistDetailScreen/${artist.permalink}/${artist.trackCount}/${artist.title}?encodedUrlString=${encodedImageUrl}"

            return url
        }

    }
}