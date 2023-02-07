package com.sema.data.model


import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("artwork_url")
    val artworkUrl: String,
    @SerializedName("artwork_url_retina")
    val artworkUrlRetina: String,
    @SerializedName("background_url")
    val backgroundUrl: String,
    @SerializedName("bpm")
    val bpm: String,
    @SerializedName("comment_count")
    val commentCount: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("download_count")
    val downloadCount: String,
    @SerializedName("download_filename")
    val downloadFilename: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    @SerializedName("downloadable")
    val downloadable: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("fan_exclusive_download")
    val fanExclusiveDownload: Int,
    @SerializedName("fan_exclusive_play")
    val fanExclusivePlay: Int,
    @SerializedName("favorited")
    val favorited: Boolean,
    @SerializedName("favoritings_count")
    val favoritingsCount: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("genre_slush")
    val genreSlush: String,
    @SerializedName("geo")
    val geo: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("license")
    val license: String,
    @SerializedName("liked")
    val liked: Boolean,
    @SerializedName("permalink")
    val permalink: String,
    @SerializedName("permalink_url")
    val permalinkUrl: String,
    @SerializedName("playback_count")
    val playbackCount: String,
    @SerializedName("played")
    val played: Boolean,
    @SerializedName("preview_url")
    val previewUrl: String,
    @SerializedName("private")
    val `private`: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("release_timestamp")
    val releaseTimestamp: Int,
    @SerializedName("reshared")
    val reshared: Boolean,
    @SerializedName("reshares_count")
    val resharesCount: String,
    @SerializedName("stream_url")
    val streamUrl: String,
    @SerializedName("taged_artists")
    val tagedArtists: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("version")
    val version: String,
    @SerializedName("waveform_data")
    val waveformData: String,
    @SerializedName("waveform_url")
    val waveformUrl: String
)

data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("caption")
    val caption: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("permalink")
    val permalink: String,
    @SerializedName("permalink_url")
    val permalinkUrl: String,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("username")
    val username: String
)