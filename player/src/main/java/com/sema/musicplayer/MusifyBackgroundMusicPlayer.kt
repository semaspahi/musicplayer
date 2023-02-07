package com.sema.musicplayer

import android.content.Context
import android.graphics.Bitmap
import com.sema.spotififi.musicplayer.utils.MediaDescriptionAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import com.sema.data.model.TrackResponse
import com.sema.domain.model.TrackResource
import com.sema.spoififi.musicplayer.utils.getCurrentPlaybackProgressFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class MusifyBackgroundMusicPlayer @Inject constructor(
    @ApplicationContext context: Context,
    private val exoPlayer: ExoPlayer
) : MusicPlayer {
    private var currentlyPlayingStreamable: TrackResource? = null
    private val notificationManagerBuilder by lazy {
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
            .setChannelImportance(NotificationUtil.IMPORTANCE_LOW)
    }

    override val currentPlaybackStateStream: Flow<MusicPlayer.PlaybackState> = callbackFlow {
        val listener = createEventsListener { player, events ->
            if (!events.containsAny(
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_PLAYER_ERROR,
                    Player.EVENT_IS_PLAYING_CHANGED,
                    Player.EVENT_IS_LOADING_CHANGED
                )
            ) return@createEventsListener
            val isPlaying =
                events.contains(Player.EVENT_IS_PLAYING_CHANGED) && player.playbackState == Player.STATE_READY && player.playWhenReady
            val isPaused =
                events.contains(Player.EVENT_IS_PLAYING_CHANGED) && player.playbackState == Player.STATE_READY && !player.playWhenReady
            val newPlaybackState = when {
                events.contains(Player.EVENT_PLAYER_ERROR) -> MusicPlayer.PlaybackState.Error
                isPlaying -> currentlyPlayingStreamable?.let { buildPlayingState(it, player) }
                isPaused -> currentlyPlayingStreamable?.let(MusicPlayer.PlaybackState::Paused)
                player.playbackState == Player.STATE_IDLE -> MusicPlayer.PlaybackState.Idle
                player.playbackState == Player.STATE_ENDED -> currentlyPlayingStreamable?.let(
                    MusicPlayer.PlaybackState::Ended
                )
                player.isLoading -> MusicPlayer.PlaybackState.Loading(previouslyPlayingStreamable = currentlyPlayingStreamable)
                else -> null
            } ?: return@createEventsListener
            trySend(newPlaybackState)
        }
        exoPlayer.addListener(listener)
        awaitClose { exoPlayer.removeListener(listener) }
        // This callback can be called multiple times on events that may
        // not be of relevance. This may lead to the generation of a new
        // state that is equivalent to the old state. Therefore use
        // distinctUntilChanged
    }.distinctUntilChanged()
        .stateIn(
            // Convert to stateflow so that new subscribers always get the latest value.
            // For example, if the user starts playing a track on the search screen
            // and moves to an album detail screen containing the same track, then
            // the subscriber associated with the detail screen can be used to
            // highlight the playing track. It is able to do so because, the first
            // value that the new subscriber gets will be the currently playing track.
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(500),
            initialValue = MusicPlayer.PlaybackState.Idle
        )

    private fun createEventsListener(onEvents: (Player, Player.Events) -> Unit) =
        object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                onEvents(player, events)
            }
        }

    private fun buildPlayingState(
        streamable: TrackResource,
        player: Player,
    ) = MusicPlayer.PlaybackState.Playing(
        currentlyPlayingStreamable = streamable,
        totalDuration = player.duration,
        currentPlaybackPositionInMillisFlow = player.getCurrentPlaybackProgressFlow()
    )

    override fun playStreamable(
        streamable: TrackResource,
        associatedAlbumArt: Bitmap
    ) {
        with(exoPlayer) {
            if (currentlyPlayingStreamable == streamable) {
                seekTo(0)
                // without this statement, after seeking to the start,
                // the player will be ready to play, but will not actually
                // start the playback if playWhenReady is set to false.
                playWhenReady = true
                return@with
            }
            if (isPlaying) exoPlayer.stop()
            currentlyPlayingStreamable = streamable
            setMediaItem(MediaItem.fromUri(streamable.streamLink))
            prepare()
            val mediaDescriptionAdapter = MediaDescriptionAdapter(
                getCurrentContentTitle = { streamable.title },
                getCurrentContentText = { streamable.detail },
                getCurrentLargeIcon = { _, _ -> associatedAlbumArt }
            )
            notificationManagerBuilder
                .setMediaDescriptionAdapter(mediaDescriptionAdapter)
                .build().setPlayer(exoPlayer)
            play()
        }
    }

    override fun pauseCurrentlyPlayingTrack() {
        exoPlayer.pause()
    }

    override fun stopPlayingTrack() {
        exoPlayer.stop()
    }

    override fun tryResume(): Boolean {
        if (exoPlayer.isPlaying) return false
        return currentlyPlayingStreamable?.let {
            exoPlayer.playWhenReady = true
            true
        } ?: false
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_NAME = "Media playback notifications"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications that contain playback controls"

        private const val NOTIFICATION_CHANNEL_ID =
            "com.example.musify.musicplayer.MusicPlayerV2Service.NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_ID = 1
    }
}