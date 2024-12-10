package com.castify.presentation.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.castify.presentation.common.Padding
import com.castify.presentation.screens.home.components.UpcomingMoviesList

val ParentPadding = PaddingValues(vertical = 16.dp, horizontal = 58.dp)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .animateContentSize()
) {
    var immersiveListHasFocus by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
    ) {
        // Section : 1 Immersive list
        item(contentType = "Upcoming Movies") {
            UpcomingMoviesList(
                modifier = Modifier.onFocusChanged {
                    immersiveListHasFocus = it.hasFocus
                }
            )
        }
    }
}

@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 8.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}

enum class ItemDirection(val aspectRatio: Float) {
    Vertical(10.5f / 16f),
    Horizontal(16f / 9f);
}