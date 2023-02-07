package com.sema.spotififi.ui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sema.data.api.ApiService
import com.sema.data.repository.ArtistsRepository
import com.sema.domain.GetPopularArtistsResourcesUseCase
import com.sema.spotififi.MainDispatcherRule
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.io.IOException
import kotlin.test.assertIs

class PopularArtistsViewModelTest {

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var repository: ArtistsRepository

    @Mock
    lateinit var getPopularArtistsResourcesUseCase: GetPopularArtistsResourcesUseCase

    @Mock
    lateinit var viewModel: PopularArtistsViewModel

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ArtistsRepository(apiService)
        getPopularArtistsResourcesUseCase = GetPopularArtistsResourcesUseCase(repository)
        viewModel = PopularArtistsViewModel(getPopularArtistsResourcesUseCase)
    }

    @Test
    fun uiStateAlbums_whenInitialized_thenShowLoading() = runTest {
        assertEquals(ArtistsUiState.Loading, viewModel.state.value)
    }

    @Test
    fun uiStatePopularArtists_whenSuccess() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        repository.getPopularTracks()
        repository.getArtist(any())

        val item = viewModel.state.value
        assertIs<ArtistsUiState.Ready>(item)
        assertEquals(item.popularArtists?.size, 1)

        collectJob.cancel()
    }



    @Test
    fun iStatePopularArtists_isFail() = runTest {
        //given
        whenever(repository.getPopularTracks()) doAnswer {
            throw IOException()
        }
        //then
        getPopularArtistsResourcesUseCase().catch {
            assertEquals(ArtistsUiState.Error(), viewModel.state.value)
        }
    }


}