package com.example.podlodkaandroidcrew.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.podlodkaandroidcrew.ui.home.HomeScreen
import com.example.podlodkaandroidcrew.ui.home.HomeViewModel
import com.example.podlodkaandroidcrew.ui.session.SessionScreen
import com.example.podlodkaandroidcrew.ui.session.SessionViewModel


@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun Navigation(
    homeViewModel: HomeViewModel,
    sessionViewModel: SessionViewModel
) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(
                viewModel = homeViewModel,
                navigateToSession = actions.navigateToSession
            )
        }
        composable(
            route = Screen.SessionScreen.route + "/{sessionId}"
        ) { backStackEntry ->
            SessionScreen(
                sessionId = backStackEntry.arguments?.getString("sessionId")!!,
                viewModel = sessionViewModel
            )
        }
    }
}

sealed class Screen (val route: String) {
    object HomeScreen : Screen("home_screen")
    object SessionScreen : Screen("session_screen")
}

class Actions (navController: NavHostController) {
    val navigateToSession: (String) -> Unit = { sessionId ->
        navController.navigate("${Screen.SessionScreen.route}/$sessionId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}