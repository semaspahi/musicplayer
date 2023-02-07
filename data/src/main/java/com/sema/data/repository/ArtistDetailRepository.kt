package com.sema.data.repository

import com.sema.data.api.ApiService
import com.sema.data.model.TrackResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class ArtistDetailRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getArtistTracks(permalink: String): List<TrackResponse> = apiService.getArtistTracks(permalink)
}