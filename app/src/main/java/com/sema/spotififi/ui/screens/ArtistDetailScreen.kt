package com.sema.spotififi.ui.screens

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sema.domain.model.TrackResource
import com.sema.spotififi.R
import com.sema.spotififi.ui.components.AsyncImageWithPlaceholder
import com.sema.spotififi.ui.components.DetailScreenTopAppBar
import com.sema.spotififi.ui.components.CompactTrackCard
import com.sema.spotififi.ui.components.DefaultLoadingAnimation
import com.sema.spotififi.ui.navigation.Screen.ArtistDetailScreen.NAV_ARG_ARTIST_NAME
import com.sema.spotififi.ui.navigation.Screen.ArtistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING
import com.sema.spotififi.ui.navigation.Screen.ArtistDetailScreen.NAV_ARG_TRACK_COUNT
import com.sema.spotififi.ui.theme.orange
import kotlinx.coroutines.launch


@Composable
fun ArtistDetailScreen(
    arguments: Bundle,
    artistDetailViewModel: ArtistDetailViewModel = hiltViewModel(),
    onTrackItemClick: (TrackResource) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    val artistDetailUiState: ArtistDetailUiState by artistDetailViewModel.state.collectAsState()

    val title = arguments.getString(NAV_ARG_ARTIST_NAME)!!
    val trackCount = arguments.getString(NAV_ARG_TRACK_COUNT)!!
    val imageUrl = arguments.getString(NAV_ARG_ENCODED_IMAGE_URL_STRING)!!

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val isAppBarVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            state = lazyListState,
        ) {
            headerWithImageItem(
                title = title,
                playlistImageUrlString = imageUrl,
                isLoadingPlaceholderForAlbumArtVisible = false,
                totalNumberOfTracks = trackCount,
                onBackButtonClicked = onBackButtonClicked
            )
            ArtistDetailContent(state = artistDetailUiState, onTrackItemClick = onTrackItemClick)

            item {
                Spacer(
                    modifier = Modifier
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .padding(bottom = 16.dp)
                )
            }
        }
        AnimatedVisibility(
            visible = isAppBarVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DetailScreenTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .statusBarsPadding(),
                title = title,
                onBackButtonClicked = onBackButtonClicked,
                onClick = {
                    coroutineScope.launch { lazyListState.animateScrollToItem(0) }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.ArtistDetailContent(
    state: ArtistDetailUiState, onTrackItemClick: (TrackResource) -> Unit,
) {
    when (state) {
        is ArtistDetailUiState.Loading -> {
            item {
                DefaultLoadingAnimation()
            }
        }
        is ArtistDetailUiState.Ready -> {
            state.popularArtists?.let { artists ->
                items(
                    items = artists,
                    itemContent = {
                        CompactTrackCard(
                            track = it,
                            onClick = onTrackItemClick,
                            isLoadingPlaceholderVisible = false,
                            isCurrentlyPlaying = false,
                            isAlbumArtVisible = false,
                            contentPadding = PaddingValues(8.dp)
                        )
                    })

            }
        }
        is ArtistDetailUiState.Error ->  state.error?.let {
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

fun LazyListScope.headerWithImageItem(
    title: String,
    playlistImageUrlString: String,
    totalNumberOfTracks: String,
    isLoadingPlaceholderForAlbumArtVisible: Boolean,
    onBackButtonClicked: () -> Unit
) {
    item {
        Column(modifier = Modifier.statusBarsPadding()) {
            ImageHeaderWithMetadata(
                title = title,
                headerImageSource = playlistImageUrlString,
                subtitle = "• $totalNumberOfTracks tracks",
                onBackButtonClicked = onBackButtonClicked,
                isLoadingPlaceholderVisible = isLoadingPlaceholderForAlbumArtVisible,
            )
            Spacer(modifier = Modifier.size(16.dp))
        }

    }
}

@Composable
fun ImageHeaderWithMetadata(
    title: String,
    headerImageSource: String,
    subtitle: String,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isLoadingPlaceholderVisible: Boolean = false,
) {
    Box(modifier) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .padding(8.dp),
            onClick = onBackButtonClicked
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_chevron_left_24),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImageWithPlaceholder(
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .shadow(8.dp),
                model = headerImageSource,
                contentDescription = null,
                isLoadingPlaceholderVisible = isLoadingPlaceholderVisible,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
                color = orange,
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption
            )
        }
    }
}
