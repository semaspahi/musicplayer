package com.sema.spotififi.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sema.data.common.asResult
import com.sema.data.common.Result
import com.sema.data.model.TrackResponse
import com.sema.domain.GetArtistDetailsResourcesUseCase
import com.sema.domain.model.TrackResource
import com.sema.spotififi.ui.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getArtistDetailsResourcesUseCase : GetArtistDetailsResourcesUseCase,
    stateHandle: SavedStateHandle,
    ) : ViewModel() {
    private var permalink = ""
    init {
        permalink = stateHandle.get(Screen.ArtistDetailScreen.NAV_ARG_PERMALINK_STRING)!!
        permalink.let { getArtistDetails(it)
        }
    }
    val state: StateFlow<ArtistDetailUiState> = getArtistDetailsResourcesUseCase.popularArtistsUiStateResult(permalink)
        .stateIn(
            scope = viewModelScope,
            initialValue = ArtistDetailUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    fun getArtistDetails(permalink: String) = viewModelScope.launch {
        getArtistDetailsResourcesUseCase(permalink)
    }
}

private fun GetArtistDetailsResourcesUseCase.popularArtistsUiStateResult(permalink: String): Flow<ArtistDetailUiState> {
    return  invoke(permalink).asResult().map{ popularArtistsResult ->
        when (popularArtistsResult) {
            is Result.Success -> ArtistDetailUiState.Ready(popularArtists = popularArtistsResult.data)
            is Result.Loading -> ArtistDetailUiState.Loading
            is Result.Error -> ArtistDetailUiState.Error(error = popularArtistsResult.exception?.message)
        }
    }
}

sealed interface ArtistDetailUiState {
    data class Ready(val popularArtists: List<TrackResource>?) : ArtistDetailUiState
    object Loading : ArtistDetailUiState
    class Error(val error: String? = null) : ArtistDetailUiState
}
