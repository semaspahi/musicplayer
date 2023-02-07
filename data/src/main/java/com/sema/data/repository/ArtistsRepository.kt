package com.sema.data.repository

import com.sema.data.api.ApiService
import com.sema.data.model.ArtistResponse
import com.sema.data.model.TrackResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class ArtistsRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPopularTracks(): List<TrackResponse> = apiService.getPopularTracks()

    suspend fun getArtist(permalink: String): ArtistResponse = apiService.getArtist(permalink)
}