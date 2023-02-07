package com.sema.spotififi.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sema.data.model.TrackResponse
import com.sema.domain.model.TrackResource
import com.sema.spotififi.R
import com.sema.spotififi.ui.theme.Black300
import com.sema.spotififi.ui.theme.SpotififiShapes

/**
 * An object that contains constants related to the [MiniPlayer]
 * composable.
 */
object MiniPlayerConstants {
    val miniPlayerHeight = 84.dp
}

/**
 * A mini player composable.
 * It also displays 2 icons - Available Devices, and Play/Pause.
 *
 * Note: The size of this composable is **fixed to 60dp**.
 *
 * @param streamable the currently [TrackResponse].
 * @param isPlaybackPaused indicates whether the playback is paused.
 * Based on this, either [onPlayButtonClicked] or [onPauseButtonClicked]
 * will be invoked. Also, the play and pause icons will also be displayed
 * based on this parameter.
 * @param modifier the modifier to be applied to this composable.
 * @param onLikedButtonClicked the lambda to execute when the like
 * button is clicked. It is provided with a boolean that indicates
 * whether the the track is currently liked or not.
 * @param onPlayButtonClicked the lambda to execute when the play button
 * is clicked.
 * @param onPauseButtonClicked the lambda to execute when the pause button
 * is clicked.
 */
@Composable
fun MiniPlayer(
    streamable: TrackResource,
    isPlaybackPaused: Boolean,
    modifier: Modifier = Modifier,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .then(modifier)
            .heightIn(
                MiniPlayerConstants.miniPlayerHeight,
                MiniPlayerConstants.miniPlayerHeight
            )
            .clip(RoundedCornerShape(20.dp)),
    ) {
        Card(
            shape = SpotififiShapes.large,
            backgroundColor = Black300,
            elevation = 10.dp,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImageWithPlaceholder(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .aspectRatio(1f),
                    model = streamable.imageUrl,
                    contentDescription = null,
                    isLoadingPlaceholderVisible = false,
                )
                Column(
                    modifier = Modifier.weight(1f).padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = streamable.title,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Text(
                        text = streamable.detail,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_available_devices),
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    if (isPlaybackPaused) {
                        // if the playback is paused, then the play button
                        // would be visible. Hence, invoke the lambda that
                        // is required to be executed when the play button
                        // is visible.
                        onPlayButtonClicked()
                    } else {
                        // Similarly, if the track is being played, then the pause
                        // button would be visible. Hence, invoke the lambda that
                        // is required to be executed when the pause button
                        // is visible.
                        onPauseButtonClicked()
                    }
                }) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .aspectRatio(1f),
                        painter = if (isPlaybackPaused) painterResource(R.drawable.ic_play_arrow_24)
                        else painterResource(R.drawable.ic_pause_24),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
