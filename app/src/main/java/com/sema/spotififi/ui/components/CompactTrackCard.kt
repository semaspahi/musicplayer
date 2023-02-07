package com.sema.spotififi.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sema.domain.model.TrackResource

/**
 * A compact card that is used to display the information of a particular
 * track. This is a wrapper over [com.example.musify.ui.components.CompactListItemCard].
 * This composable is mainly responsible for providing two
 * important functionalities:
 * 1) Setting the content alpha based on whether the track is available.
 * 2) Setting the text style of the composable based on whether it is
 * playing or not.
 *
 * @param onClick the callback to execute when the card is clicked.
 * @param isLoadingPlaceholderVisible indicates whether the loading
 * placeholder is visible for the thumbnail image.
 * @param modifier [Modifier] to be applied to the card.
 * @param isCurrentlyPlaying indicates whether the [track] is the currently
 * playing track, based on which, the style of the card will be set.
 * @param isAlbumArtVisible indicates whether the album art is visible
 * or not.
 * @param titleTextStyle The style configuration for the title of the
 * track.
 * @param subtitleTextStyle The style configuration for the subtitle of
 * the track.
 */
@ExperimentalMaterialApi
@Composable
fun CompactTrackCard(
    track: TrackResource,
    onClick: (TrackResource) -> Unit,
    isLoadingPlaceholderVisible: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    backgroundColor: Color = MaterialTheme.colors.background,
    isCurrentlyPlaying: Boolean = false,
    isAlbumArtVisible: Boolean = true,
    titleTextStyle: TextStyle = LocalTextStyle.current,
    subtitleTextStyle: TextStyle = LocalTextStyle.current,
    contentPadding: PaddingValues = CompactTrackCardDefaults.defaultContentPadding
) {
    val trackPlayingTextStyle = LocalTextStyle.current.copy(
        color = MaterialTheme.colors.background
    )
    // set alpha based on whether the track is available for playback
    CompositionLocalProvider(
        LocalContentAlpha.provides(1f)
    ) {
        MusifyCompactListItemCard(
            modifier = modifier.padding(4.dp),
            backgroundColor = backgroundColor,
            shape = shape,
            cardType = ListItemCardType.TRACK,
            thumbnailImageUrlString = track.imageUrl ,
            title = track.title,
            subtitle = track.detail,
            onClick = { onClick(track) },
            onTrailingButtonIconClick = {},
            isLoadingPlaceHolderVisible = isLoadingPlaceholderVisible,
            onThumbnailLoading = {},
            onThumbnailImageLoadingFinished = { throwable -> },
            titleTextStyle = if (isCurrentlyPlaying) trackPlayingTextStyle else titleTextStyle,
            subtitleTextStyle = subtitleTextStyle,
            contentPadding = contentPadding
        )
    }
}

/**
 * Contains default values used by [CompactTrackCard].
 */
object CompactTrackCardDefaults {
    val defaultContentPadding = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp
    )
}