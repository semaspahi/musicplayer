package com.sema.spotififi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sema.domain.model.PopularArtistsResource

import com.sema.spotififi.R
import com.sema.spotififi.ui.components.CompactTrackCard
import com.sema.spotififi.ui.components.DefaultLoadingAnimation
import com.sema.spotififi.ui.theme.SpotififiShapes
import com.sema.spotififi.ui.theme.gray
import com.sema.spotififi.ui.theme.orange


@Composable
fun PopularArtistsScreen(
    popularArtistsViewModel: PopularArtistsViewModel = hiltViewModel(),
    onClick: (PopularArtistsResource) -> Unit
) {
    val artistsUiState: ArtistsUiState by popularArtistsViewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(bottom = 16.dp),
            state = lazyListState
        ) {
            headerTitle()
            PopularArtistsContent(state = artistsUiState, onArtistClick = onClick,)
        }
    }
}

fun LazyListScope.headerTitle() {
    item {
        Column(modifier = Modifier.statusBarsPadding()) {
            Text(
                text = stringResource(R.string.popular_artists_screen_title),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(32.dp),
                color = orange
            )
        }
    }
}

fun LazyListScope.PopularArtistsContent(
    state: ArtistsUiState, onArtistClick: (PopularArtistsResource) -> Unit,
) {
    when (state) {
        is ArtistsUiState.Loading -> {
            item {
                DefaultLoadingAnimation()
            }
        }
        is ArtistsUiState.Ready -> {
            state.popularArtists?.let {
                items(
                    items = it,
                    itemContent = {
                        ListItem(item = it, onClick = onArtistClick)
                    })

            }
        }
        is ArtistsUiState.Error -> state.error?.let {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Oops! Something doesn't look right",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle2
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistImage(item: PopularArtistsResource) {
    Image(
        painter = rememberAsyncImagePainter(item.imageUrl),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(78.dp)
            .padding(8.dp)
            .clip(CircleShape)
    )
}

@Composable
fun ListItem(item: PopularArtistsResource, onClick: (PopularArtistsResource) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, top = 12.dp, bottom= 12.dp , end = 16.dp)
            .clickable { onClick.invoke(item) }
    ) {

        ArtistImage(item)
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(0.55f)
                .padding(8.dp)
                .wrapContentHeight()
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Medium,
                color = orange,
                style = MaterialTheme.typography.subtitle1,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.popular_artists_screen_item_song_count,
                    item.trackCount
                ),
                color = gray,
                style = MaterialTheme.typography.subtitle2,
            )
            Spacer(modifier = Modifier.height(4.dp))

            item.content?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.caption,
                    maxLines = 2
                )
            }

        }
    }
}

