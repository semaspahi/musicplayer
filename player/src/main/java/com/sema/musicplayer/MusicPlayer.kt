package com.sema.musicplayer

import android.graphics.Bitmap
import com.sema.data.model.TrackResponse
import com.sema.domain.model.TrackResource
import kotlinx.coroutines.flow.Flow

interface MusicPlayer {
    sealed class PlaybackState(open val currentlyPlayingStreamable: TrackResource? = null) {
        data class Loading(val previouslyPlayingStreamable: TrackResource?) : PlaybackState()
        data class Playing(
            override val currentlyPlayingStreamable: TrackResource,
            val totalDuration: Long,
            val currentPlaybackPositionInMillisFlow: Flow<Long>
        ) : PlaybackState()

        data class Paused(override val currentlyPlayingStreamable: TrackResource) : PlaybackState()
        data class Ended(val streamable: TrackResource) : PlaybackState()
        object Error : PlaybackState()
        object Idle : PlaybackState()
    }

    val currentPlaybackStateStream: Flow<PlaybackState>
    fun playStreamable(streamable: TrackResource, associatedAlbumArt: Bitmap)
    fun pauseCurrentlyPlayingTrack()
    fun stopPlayingTrack()
    fun tryResume(): Boolean
}