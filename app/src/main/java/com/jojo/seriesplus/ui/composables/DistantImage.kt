package com.jojo.seriesplus.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
fun DistantImage(
    modifier: Modifier = Modifier,
    url: String?,
    contentScale: ContentScale = ContentScale.FillHeight,
    elevation: Dp = 0.dp
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .shadow(if (imageState is AsyncImagePainter.State.Success) elevation else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier,
            onState = { imageState = it },
            contentScale = contentScale
        )

        when (imageState) {
            is AsyncImagePainter.State.Error -> Icon(Icons.Default.Error, contentDescription = null)
            is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
            else -> {}
        }
    }
}