package com.castify.presentation.screens.movieDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.castify.data.dto.TMDBMovie
import com.castify.presentation.common.CastifyOutlineIconButton
import com.castify.presentation.common.GradientBackground
import com.castify.presentation.common.PosterImage
import com.castify.ui.CastifyStartContainerPadding
import com.castify.ui.CastifyTopContainerPadding

@Composable
fun MovieDetailsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        PosterImage(
            modifier = Modifier
                .width(650.dp)
                .height(400.dp)
                .align(Alignment.TopEnd),
            movie = TMDBMovie(
                id = "1",
                name = "On the bridge",
                imageUri = "https://storage.googleapis.com/androiddevelopers/samples/media/posters/2_3-300/on-the-bridge.jpg"
            )
        )

        GradientBackground(
            modifier = Modifier.fillMaxSize(),
            gradientColor = MaterialTheme.colorScheme.background
        )

        Column(
            modifier = Modifier
                .padding(
                    start = CastifyStartContainerPadding,
                    top = CastifyTopContainerPadding
                )
                .fillMaxWidth(0.4f)
        ) {
            Text(
                text = "Avatar: The way of water",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = "Subtitle",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = "Avatar: The way of water",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(50.dp))

            CastifyOutlineIconButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.PlayArrow,
                roundPercentage = 20,
                isFocusedGlow = true,
                buttonText = "Watch now",
                onClick = {}
            )

            Spacer(modifier = Modifier.size(20.dp))

            CastifyOutlineIconButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.FavoriteBorder,
                roundPercentage = 20,
                isFocusedGlow = true,
                buttonText = "Add to Favourites",
                onClick = {}
            )
        }

    }
}