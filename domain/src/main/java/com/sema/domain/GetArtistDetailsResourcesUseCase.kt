/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sema.domain

import com.sema.data.repository.ArtistDetailRepository
import com.sema.domain.model.mapToTrackResources
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * A use case responsible for obtaining popular artists with their informations
 */
class GetArtistDetailsResourcesUseCase @Inject constructor(
    private val artistDetailRepository: ArtistDetailRepository,
) {

    /**
     * Returns a list of tracks from the popular artist
     */
    operator fun invoke(permalink: String) = flow {
        val trackResponse = artistDetailRepository.getArtistTracks(permalink)
        emit(trackResponse.mapToTrackResources())
    }

}