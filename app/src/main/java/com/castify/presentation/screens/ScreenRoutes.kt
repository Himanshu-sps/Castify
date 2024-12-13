package com.castify.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed class ScreenRoutes {
    @Serializable
    data object DashboardScreenRoute: ScreenRoutes()

    @Serializable
    data object SearchScreenRoute: ScreenRoutes()

    @Serializable
    data object HomeScreenRoute: ScreenRoutes()

    @Serializable
    data object FavouriteScreenRoute: ScreenRoutes()

    @Serializable
    data object SettingsScreenRoute: ScreenRoutes()
}

enum class DrawerItems(
    val screenRoutes: ScreenRoutes,
    val screenName: String,
    val tabIcon: ImageVector
) {
    SearchScreenRoute(screenRoutes = ScreenRoutes.SearchScreenRoute, screenName = "Search", tabIcon = Icons.Default.Search),
    HomeScreenRoute(screenRoutes = ScreenRoutes.HomeScreenRoute, screenName = "Home", tabIcon = Icons.Default.Home),
    FavouriteScreenRoute(screenRoutes = ScreenRoutes.FavouriteScreenRoute, screenName = "Favourites", tabIcon = Icons.Default.Favorite),
    SettingsScreenRoute(screenRoutes = ScreenRoutes.SettingsScreenRoute, screenName = "Settings", tabIcon = Icons.Default.Settings);
}

/**
 * Extension function which return the class name
 * @param T
 * @return class Name
 */
inline fun <reified T : Any> T.className(): String {
    return T::class.simpleName ?: "DashboardScreenRoute"
}