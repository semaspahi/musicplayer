package com.sema.spotififi.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sema.data.model.TrackResponse
import com.sema.domain.model.TrackResource
import kotlinx.coroutines.flow.Flow

/**
 * A mini player that can expand to fill the entire screen when clicked.
 * This composable also contains a snack bar. The snack bar will be
 * displayed on top of the mini player if it is collapsed. If the
 * mine player is expanded, then the snack bar will be displayed
 * at the bottom of the screen.
 *
 * @param streamable the [TrackResponse] to be displayed.
 * @param onPauseButtonClicked the lambda to execute when the pause button
 * is clicked.
 * @param onPlayButtonClicked the lambda to execute when the play button
 * is clicked.
 * @param isPlaybackPaused indicates whether the playback is paused. Based on
 * this, the play/pause button will be shown.
 * @param timeElapsedStringFlow a [Flow] that emits a stream of strings
 * that represent the time elapsed.
 * @param playbackProgressFlow a [Flow] that emits the current playback progress.
 * @param totalDurationOfCurrentTrackText represents the total duration of the
 * currently playing track as a string.
 * @param modifier the modifier to be applied to the composable.
 * @param snackbarHostState the [SnackbarHostState] that will be used for
 * handing the snackbar used in this composable.
 */
@ExperimentalAnimationApi
@Composable
fun ExpandableMiniPlayerWithSnackbar(
    streamable: TrackResource,
    onPauseButtonClicked: () -> Unit,
    onPlayButtonClicked: (TrackResource) -> Unit,
    isPlaybackPaused: Boolean,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Column {
        SnackbarHost(
            modifier = Modifier.padding(8.dp),
            hostState = snackbarHostState
        )
        MiniPlayer(
            modifier = Modifier
                .padding(bottom = 32.dp),
            isPlaybackPaused = isPlaybackPaused,
            streamable = streamable,
            onPlayButtonClicked = { onPlayButtonClicked(streamable) },
            onPauseButtonClicked = onPauseButtonClicked
        )
    }
}

