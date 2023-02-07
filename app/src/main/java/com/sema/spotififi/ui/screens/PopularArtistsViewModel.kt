package com.sema.spotififi.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sema.data.common.asResult
import com.sema.data.common.Result
import com.sema.domain.GetPopularArtistsResourcesUseCase
import com.sema.domain.model.PopularArtistsResource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularArtistsViewModel @Inject constructor(
    private val getPopularArtistsResourcesUseCase : GetPopularArtistsResourcesUseCase,
) : ViewModel() {

    val state: StateFlow<ArtistsUiState> = getPopularArtistsResourcesUseCase.popularArtistsUiStateResult()
        .stateIn(
            scope = viewModelScope,
            initialValue = ArtistsUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    init {
        getPopularArtists()
    }

    fun getPopularArtists() = viewModelScope.launch {
        getPopularArtistsResourcesUseCase()
    }
}

private fun GetPopularArtistsResourcesUseCase.popularArtistsUiStateResult(): Flow<ArtistsUiState> {
    return  invoke().asResult().map{ popularArtistsResult ->
        when (popularArtistsResult) {
            is Result.Success -> ArtistsUiState.Ready(popularArtists = popularArtistsResult.data)
            is Result.Loading -> ArtistsUiState.Loading
            is Result.Error -> ArtistsUiState.Error(error = popularArtistsResult.exception?.message)
        }
    }
}

sealed interface ArtistsUiState {
    data class Ready(val popularArtists: List<PopularArtistsResource>?) : ArtistsUiState
    object Loading : ArtistsUiState
    class Error(val error: String? = null) : ArtistsUiState
}
