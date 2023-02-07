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

import com.sema.data.model.TrackResponse

/**
 * A [TrackResource] with Track response information
 */
data class TrackResource internal constructor(
    val title: String,
    val detail: String,
    val imageUrl: String,
    val streamLink: String,
    ) {
    constructor(trackResponse: TrackResponse) : this(
        title = trackResponse.title,
        detail = trackResponse.description,
        imageUrl = trackResponse.artworkUrl,
        streamLink = trackResponse.streamUrl,
    )
}

fun List<TrackResponse>.mapToTrackResources(): List<TrackResource> = map { TrackResource(it) }

