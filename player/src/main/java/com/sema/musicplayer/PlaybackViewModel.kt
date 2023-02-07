package com.sema.musicplayer

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sema.data.model.TrackResponse
import com.sema.domain.model.TrackResource
import com.sema.musicplayer.domain.DownloadDrawableFromUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    application: Application,
    private val musicPlayer: MusicPlayer,
    private val downloadDrawableFromUrlUseCase: DownloadDrawableFromUrlUseCase
) : AndroidViewModel(application) {

    private val _totalDurationOfCurrentTrackTimeText = mutableStateOf("00:00")
    val totalDurationOfCurrentTrackTimeText = _totalDurationOfCurrentTrackTimeText as State<String>

    private val _playbackState = mutableStateOf<PlaybackState>(PlaybackState.Idle)
    val playbackState = _playbackState as State<PlaybackState>

    private val _eventChannel = Channel<Event?>()
    val playbackEventsFlow = _eventChannel.receiveAsFlow()

    // 0f to 100f
    val flowOfProgressOfCurrentTrack = mutableStateOf<Flow<Float>>(emptyFlow())
    val flowOfProgressTextOfCurrentTrack = mutableStateOf<Flow<String>>(emptyFlow())

    private val playbackErrorMessage = "An error occurred. Please check internet connection."

    init {
        musicPlayer.currentPlaybackStateStream.onEach {
            _playbackState.value = when (it) {
                is MusicPlayer.PlaybackState.Loading -> PlaybackState.Loading(it.previouslyPlayingStreamable)
                is MusicPlayer.PlaybackState.Idle -> PlaybackState.Idle
                is MusicPlayer.PlaybackState.Playing -> {
                    _totalDurationOfCurrentTrackTimeText.value =
                        convertTimestampMillisToString(it.totalDuration)
                    flowOfProgressOfCurrentTrack.value =
                        it.currentPlaybackPositionInMillisFlow.map { progress -> (progress.toFloat() / it.totalDuration) * 100f }
                    flowOfProgressTextOfCurrentTrack.value =
                        it.currentPlaybackPositionInMillisFlow.map(::convertTimestampMillisToString)
                    PlaybackState.Playing(it.currentlyPlayingStreamable)
                }
                is MusicPlayer.PlaybackState.Paused -> PlaybackState.Paused(it.currentlyPlayingStreamable)
                is MusicPlayer.PlaybackState.Error -> {
                    viewModelScope.launch {
                        _eventChannel.send(Event.PlaybackError(playbackErrorMessage))
                    }
                    PlaybackState.Error(playbackErrorMessage)
                }
                is MusicPlayer.PlaybackState.Ended -> PlaybackState.PlaybackEnded(it.streamable)
            }
        }.launchIn(viewModelScope)
    }

    fun resumeIfPaused() {
        musicPlayer.tryResume()
    }

    fun playStreamable(streamable: TrackResource) {
        viewModelScope.launch {

            val downloadAlbumArtResult = downloadDrawableFromUrlUseCase.invoke(
                urlString = streamable.imageUrl,
                context = getApplication()
            )
            if (downloadAlbumArtResult.isSuccess) {
                // getOrNull() can't be null because this line is executed
                // if, and only if the image was downloaded successfully.
                val bitmap = downloadAlbumArtResult.getOrNull()!!.toBitmap()
                musicPlayer.playStreamable(
                    streamable = streamable,
                    associatedAlbumArt = bitmap
                )
            } else {
                _eventChannel.send(Event.PlaybackError(playbackErrorMessage))
                _playbackState.value = PlaybackState.Error(playbackErrorMessage)
            }
        }
    }

    fun pauseCurrentlyPlayingTrack() {
        musicPlayer.pauseCurrentlyPlayingTrack()
    }

    private fun convertTimestampMillisToString(millis: Long): String = with(TimeUnit.MILLISECONDS) {
        // don't display the hour information if the track's duration is
        // less than an hour
        if (toHours(millis) == 0L) "%02d:%02d".format(
            toMinutes(millis), toSeconds(millis)
        )
        else "%02d%02d:%02d".format(
            toHours(millis), toMinutes(millis), toSeconds(millis)
        )
    }

    companion object {
        val PLAYBACK_PROGRESS_RANGE = 0f..100f
    }

    sealed class PlaybackState(
        val currentlyPlayingStreamable: TrackResource? = null,
        val previouslyPlayingStreamable: TrackResource? = null
    ) {
        object Idle : PlaybackState()
        object Stopped : PlaybackState()
        data class Error(val errorMessage: String) : PlaybackState()
        data class Paused(val streamable: TrackResource) : PlaybackState(streamable)
        data class Playing(val streamable: TrackResource) : PlaybackState(streamable)
        data class PlaybackEnded(val streamable: TrackResource) : PlaybackState(streamable)
        data class Loading(
            // Streamable instance that indicates the track that was playing before
            // the state was changed to loading
            val previousStreamable: TrackResource?
        ) : PlaybackState(previouslyPlayingStreamable = previousStreamable)
    }

    sealed class Event {
        // a data class is not used because a 'Channel' will not send
        // two items of the same type consecutively. Since a data class
        // overrides equals & hashcode by default, if the same event
        // occurs consecutively, the event will not be sent over the
        // channel, resulting in missed events.
        class PlaybackError(val errorMessage: String) : Event()
    }
}
