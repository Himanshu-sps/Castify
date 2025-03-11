package com.castify.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.DrawerState
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.NavigationDrawerItemDefaults
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import com.castify.data.dto.MovieDetailsDTO
import com.castify.presentation.common.handleDPadPreviewKeyEvents
import com.castify.presentation.screens.DrawerItems
import com.castify.presentation.screens.ScreenRoutes
import com.castify.presentation.screens.favourites.FavouriteScreen
import com.castify.presentation.screens.home.HomeScreen
import com.castify.presentation.screens.home.HomeViewModel
import com.castify.presentation.screens.search.SearchScreen
import com.castify.presentation.screens.settings.SettingsScreen
import com.castify.ui.Gray

val drawerItems = DrawerItems.entries.toList()

@Composable
fun DashboardScreen(
    homeViewModel: HomeViewModel,
    onMovieClick: (MovieDetailsDTO) -> Unit,
    onBackTriggered: () -> Unit
) {

    val drawerItemFocusRequesters = remember { drawerItems.map { FocusRequester() } }

    val navController = rememberNavController()
    var currentRoute: String by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isDrawerItemFocused by remember { mutableStateOf(false) }

    val currentDrawerSelectedIndex by remember(currentRoute) {
        derivedStateOf {
            if (currentRoute.isBlank()) {
                1
            } else {
                val value = DrawerItems.entries.toList()
                    .first { currentRoute.contains(it.screenRoutes.toString(), true) }
                drawerItems.indexOf(value)
            }
        }
    }

    /**
     * When dashboard screen went off we need to remove attached listener as well
     */
    DisposableEffect(Unit) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            currentRoute = destination.route.toString()
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    BackPressHandledArea(
        onBackPressed = {
            // 1. If drawer is opened then on back pressed close the drawer and focus selected drawer option
            if (drawerState.currentValue == DrawerValue.Open) {
                drawerState.setValue(DrawerValue.Closed)
                drawerItemFocusRequesters[currentDrawerSelectedIndex].requestFocus()
            }
            else if (currentDrawerSelectedIndex == 1) onBackTriggered()
            else if (!isDrawerItemFocused) {
                drawerItemFocusRequesters[currentDrawerSelectedIndex].requestFocus()
            } else {
                drawerItemFocusRequesters[1].requestFocus()
                //navController.popBackStack(route = ScreenRoutes.HomeScreenRoute, inclusive = true)
                navController.navigate(route = ScreenRoutes.HomeScreenRoute)
            }
        },
        mainScreenContent = {
            /**
             * To focus back to selected item
             */
            LaunchedEffect(key1 = currentDrawerSelectedIndex) {
                drawerItemFocusRequesters[currentDrawerSelectedIndex].requestFocus()
            }

            /** Navigation Drawer **/
            DashboardNavigationDrawer(
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        isDrawerItemFocused = focusState.isFocused
                    },
                homeViewModel = homeViewModel,
                drawerItemFocusRequesters = drawerItemFocusRequesters,
                drawerState = drawerState,
                navController = navController,
                selectedDrawerItemIndex = currentDrawerSelectedIndex,
                onMovieClick = onMovieClick,
                onScreenSelection = { screenRoute ->

                    // On every screen selection hiding drawer if opened
                    if (drawerState.currentValue == DrawerValue.Open) {
                        drawerState.setValue(DrawerValue.Closed)
                    }

                    //if (!currentRoute.contains(screenRoute.toString(), true)) {
                        navController.navigate(route = screenRoute) {
                            if (screenRoute.toString().contains(ScreenRoutes.HomeScreenRoute.toString())) {
                                popUpTo(
                                    route = ScreenRoutes.HomeScreenRoute
                                )
                            }
                            launchSingleTop = true
                        }
                    //}
                }
            )
        }
    )
}

@Composable
fun DashboardNavigationDrawer(
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    drawerItemFocusRequesters: List<FocusRequester>,
    drawerState: DrawerState,
    navController: NavHostController,
    selectedDrawerItemIndex: Int,
    onMovieClick: (MovieDetailsDTO) -> Unit,
    onScreenSelection: (ScreenRoutes) -> Unit
) {

    val backgroundContentPadding = 8.dp

    ModalNavigationDrawer(
        modifier = modifier,
        scrimBrush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.background,
                Color.Transparent
            )
        ),
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = modifier
                    .background(
                        color = if (drawerState.currentValue == DrawerValue.Open) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .fillMaxHeight()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = Color.Gray,
                            shape = RoundedCornerShape(percent = 50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                LazyColumn(
                    modifier = modifier.selectableGroup(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(drawerItems) { index, item ->

                        NavigationDrawerItem(
                            modifier = modifier
                                .padding(end = backgroundContentPadding)
                                .focusRequester(drawerItemFocusRequesters[index])
                                .handleDPadPreviewKeyEvents(
                                    onLeft = {
                                        drawerItemFocusRequesters[selectedDrawerItemIndex].requestFocus()
                                    }
                                ),
                            selected = selectedDrawerItemIndex == index,
                            colors = NavigationDrawerItemDefaults.colors(
                                focusedSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                focusedContainerColor = Gray
                            ),
                            onClick = {
                                onScreenSelection(item.screenRoutes)
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = item.tabIcon,
                                    contentDescription = item.tabIcon.name,
                                )
                            },
                            content = {
                                Text(
                                    text = item.screenName,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    }
                }
            }
        },
        content = {
            /** Body ***/
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                NavComposable(
                    homeViewModel = homeViewModel,
                    navController = navController,
                    onMovieClick = onMovieClick
                )
            }

        }
    )
}

@Composable
private fun NavComposable(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onMovieClick: (MovieDetailsDTO) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val closeDrawerWidth = 64.dp

    NavHost(
        modifier = modifier.padding(
            start = closeDrawerWidth
        ),
        navController = navController,
        startDestination = ScreenRoutes.HomeScreenRoute,
    ) {
        composable<ScreenRoutes.HomeScreenRoute> {
            val state by homeViewModel.state.collectAsStateWithLifecycle()
            HomeScreen(
                homeListState = state,
                onMovieClick = onMovieClick
            )
        }

        composable<ScreenRoutes.SearchScreenRoute> {
            SearchScreen()
        }

        composable<ScreenRoutes.FavouriteScreenRoute> {
            FavouriteScreen()
        }

        composable<ScreenRoutes.SettingsScreenRoute> {
            SettingsScreen()
        }
    }
}

@Composable
private fun BackPressHandledArea(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    mainScreenContent: @Composable BoxScope.() -> Unit,
) =
    Box(
        modifier = Modifier
            .onPreviewKeyEvent {
                if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                    onBackPressed()
                    true
                } else {
                    false
                }
            }
            .then(modifier),
        content = mainScreenContent
    )