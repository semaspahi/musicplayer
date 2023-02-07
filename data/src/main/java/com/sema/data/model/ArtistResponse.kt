package com.sema.data.model


import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("background_url")
    val backgroundUrl: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("followers_count")
    val followersCount: Int,
    @SerializedName("following")
    val following: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("likes_count")
    val likesCount: Int,
    @SerializedName("permalink")
    val permalink: String,
    @SerializedName("permalink_url")
    val permalinkUrl: String,
    @SerializedName("playlist_count")
    val playlistCount: Int,
    @SerializedName("track_count")
    val trackCount: Int,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("username")
    val username: String
)