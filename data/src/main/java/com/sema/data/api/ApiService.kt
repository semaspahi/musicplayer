package com.sema.data.api

import com.sema.data.model.ArtistResponse
import com.sema.data.model.TrackResponse
import retrofit2.http.*

interface ApiService {

    @GET("/feed?type=popular&page=1&count=5")
    suspend fun getPopularTracks(): List<TrackResponse>

    @GET("/{permalink}")
    suspend fun getArtist(@Path("permalink") permalink: String): ArtistResponse

    @GET("/{permalink}/?type=tracks&page=1&count=10")
    suspend fun getArtistTracks(@Path("permalink") permalink: String): List<TrackResponse>

}
