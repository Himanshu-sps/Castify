package com.castify.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.castify.presentation.common.ifElse
import com.castify.presentation.screens.dashboard.DashboardScreen
import com.castify.presentation.screens.movieDetails.MovieDetailsScreen
import com.castify.ui.LightBlue

@Composable
fun App(
    onBackTriggered: () -> Unit
) {

    /********* Navigation *********/
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.DashboardScreenRoute,
        builder = {
            composable<ScreenRoutes.DashboardScreenRoute> {
                DashboardScreen(
                    onBackTriggered = onBackTriggered,
                    onMovieClick = { movie ->
                        navController.navigate(
                            route = ScreenRoutes.MovieDetailsScreen(movieId = movie.id.toString())
                        )
                    }
                )
            }

            composable<ScreenRoutes.MovieDetailsScreen> {
                val args = it.toRoute<ScreenRoutes.MovieDetailsScreen>()
                val movieId = args.movieId

                MovieDetailsScreen()
            }
        }
    )
    /********* Navigation *********/
}

@Composable
fun CustomDrawerItemIndicator(
    isFocused: Boolean = false,
    content: (@Composable () -> Unit)? = null
) {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .ifElse(
                condition = isFocused,
                ifTrueModifier = Modifier.background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            LightBlue.copy(alpha = 0.3f),
                            LightBlue.copy(alpha = 0.5f)
                        )
                    )
                ),
                ifFalseModifier = Modifier
            )
    ) {
        content?.invoke()
    }
}