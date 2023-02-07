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

package com.sema.domain.model

import com.sema.data.model.ArtistResponse
import com.sema.data.model.TrackResponse

/**
 * A [PopularArtistsResource] with additional Artist and Track response information
 */
data class PopularArtistsResource internal constructor(
    val permalink: String,
    val title: String,
    val content: String?,
    val trackCount: Int,
    val imageUrl: String,
) {
    constructor(artistResponse: ArtistResponse, trackResponse: TrackResponse) : this(
        permalink = trackResponse.user.permalink,
        title = artistResponse.username,
        content = artistResponse.description,
        imageUrl = artistResponse.avatarUrl,
        trackCount = artistResponse.trackCount,
    )
}
