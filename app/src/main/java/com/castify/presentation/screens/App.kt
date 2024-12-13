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
import com.castify.presentation.common.ifElse
import com.castify.presentation.screens.dashboard.DashboardScreen
import com.castify.ui.LightBlue

@Composable
fun App(
    onBackPressed: () -> Unit
) {

    /********* Navigation *********/
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.DashboardScreenRoute,
        builder = {
            composable<ScreenRoutes.DashboardScreenRoute> {
                DashboardScreen(
                    onBackPressed = {}
                )
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