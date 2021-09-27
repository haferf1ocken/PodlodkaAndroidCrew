package com.example.podlodkaandroidcrew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.primarySurface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import coil.annotation.ExperimentalCoilApi
import com.example.podlodkaandroidcrew.ui.home.HomeViewModel
import com.example.podlodkaandroidcrew.ui.navigation.Navigation
import com.example.podlodkaandroidcrew.ui.session.SessionViewModel
import com.example.podlodkaandroidcrew.ui.theme.PodlodkaAndroidCrewTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.Factory((application as PodlodkaAndroidCrewApplication).repository)
    }
    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModel.Factory((application as PodlodkaAndroidCrewApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodlodkaAndroidCrewTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
                    val darkTheme = isSystemInDarkTheme()
                    SideEffect {
                        systemUiController.setNavigationBarColor(Color.Transparent)
                        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = !darkTheme)
                    }
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        Navigation(
                            homeViewModel = homeViewModel,
                            sessionViewModel = sessionViewModel
                        )
                    }
                }
            }
        }
    }
}