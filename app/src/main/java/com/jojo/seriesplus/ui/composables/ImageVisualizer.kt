package com.jojo.seriesplus.ui.composables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageVisualizer(
    url: List<String>,
    index: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClose: () -> Unit
) {
    with(animatedVisibilityScope) {

        BackHandler {
            onClose()
        }

        var i by remember { mutableStateOf(index) }
        var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

        Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray.copy(.4f)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onClose,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(enabled = false, onClick = {})
                ) {
                    var scale by remember { mutableStateOf(1f) }
                    var offset by remember { mutableStateOf(Offset.Zero) }
                    val state =
                        rememberTransformableState { zoomChange, offsetChange, _ ->
                            scale *= zoomChange
                            offset += offsetChange * 1.3f
                        }

                    LaunchedEffect(state.isTransformInProgress) {
                        if (!state.isTransformInProgress) {
                            scale = 1f
                            offset = Offset.Zero
                        }
                    }

                    AsyncImage(
                        model = url[i],
                        contentDescription = null,
                        onState = { imageState = it },
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateEnterExit(
                                enter = fadeIn() + scaleIn(initialScale = .6f),
                                exit = fadeOut() + scaleOut()
                            )
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                            .transformable(state = state)
                    )

                    when (imageState) {
                        is AsyncImagePainter.State.Error -> Icon(
                            Icons.Default.Error,
                            contentDescription = null
                        )

                        is AsyncImagePainter.State.Loading ->
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                        else -> {}
                    }
                }

                if (url.size > 1) {
                    IconButton(
                        onClick = { i-- },
                        enabled = i > 0,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(Icons.Default.ArrowBackIos, contentDescription = null)
                    }
                    IconButton(
                        onClick = { i++ },
                        enabled = i < url.size - 1,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null)
                    }
                }
            }
        }
    }
}