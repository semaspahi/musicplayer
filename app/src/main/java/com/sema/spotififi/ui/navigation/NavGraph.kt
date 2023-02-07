package com.sema.spotififi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sema.data.model.TrackResponse
import com.sema.domain.model.PopularArtistsResource
import com.sema.domain.model.TrackResource
import com.sema.spotififi.ui.screens.ArtistDetailScreen
import com.sema.spotififi.ui.screens.PopularArtistsScreen

@Composable
fun NavGraph(
    playStreamable: (TrackResource) -> Unit,
    onPausePlayback: () -> Unit,
) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(navController, startDestination = Screen.PopularArtists.route) {
        composable(Screen.PopularArtists.route) {
            PopularArtistsScreen(onClick = { actions.goToArtistDetailScreen(it) })
        }
        composable(Screen.ArtistDetailScreen.route) {
            val arguments = it.arguments!!
            ArtistDetailScreen(
                arguments,
                onTrackItemClick = {playStreamable.invoke(it) },
                onBackButtonClicked = { actions.popBackStack.invoke() }
            )
        }
    }
}

class MainActions(private val navController: NavHostController) {

    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }
    val goToPopularArtistsScreen: () -> Unit = {
        navController.navigate(Screen.PopularArtists.route)
    }

    val goToArtistDetailScreen: (PopularArtistsResource) -> Unit = {
        navController.navigate(Screen.ArtistDetailScreen.buildRoute(it))
    }

}