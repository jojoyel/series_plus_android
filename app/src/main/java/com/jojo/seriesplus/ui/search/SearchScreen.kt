package com.jojo.seriesplus.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.TableRows
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jojo.seriesplus.R
import com.jojo.seriesplus.ui.composables.CardDisplayType
import com.jojo.seriesplus.ui.composables.ResultItem
import com.jojo.seriesplus.util.Routes
import kotlinx.coroutines.job

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<SearchViewModel>()

    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var lazyDisplayType by rememberSaveable { mutableStateOf(LazyDisplayType.GRID) }

    Surface(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .1f)) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchRow(
                query = query,
                onQueryChanged = { viewModel.onQueryChanged(it) })

            if (query.isNotEmpty() && results.isNotEmpty())
                Box(modifier = Modifier.weight(1f)) {
                    when (lazyDisplayType) {
                        LazyDisplayType.GRID -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(results) {
                                    ResultItem(
                                        data = it,
                                        displayType = CardDisplayType.SQUARE,
                                        modifier = Modifier.height(IntrinsicSize.Max)
                                    ) {
                                        navController.navigate("${Routes.Details.route}/${it.id}")
                                    }
                                }
                            }
                        }

                        LazyDisplayType.COLUMN -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(items = results) {
                                    ResultItem(
                                        data = it,
                                        modifier = Modifier.animateItemPlacement()
                                    ) {
                                        navController.navigate("${Routes.Details.route}/${it.id}")
                                    }
                                }
                            }
                        }
                    }

                    ToggleButton(
                        position = if (lazyDisplayType == LazyDisplayType.GRID) 0 else 1,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        orientation = ToggleButtonOrientation.COLUMN,
                        startIcon = Icons.Rounded.GridView,
                        endIcon = Icons.Rounded.TableRows,
                        onClicked = {
                            lazyDisplayType =
                                if (lazyDisplayType == LazyDisplayType.GRID) LazyDisplayType.COLUMN else LazyDisplayType.GRID
                        })

                    if (isLoading)
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(100),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .alpha(.7f)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(18.dp)
                                    .size(58.dp),
                                strokeWidth = 9.dp
                            )
                        }
                }
            else if (query.isEmpty())
                Text(stringResource(R.string.search_tip), Modifier.padding(8.dp))
            else
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        stringResource(R.string.search_no_result, query),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
        }
    }
}

@Composable
fun SearchRow(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        if (query.isBlank())
            this.coroutineContext.job.invokeOnCompletion { focusRequester.requestFocus() }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth(.9f)
                .padding(vertical = 6.dp)
                .then(modifier)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { onQueryChanged(it) },
                    modifier = Modifier.focusRequester(focusRequester),
                    singleLine = true,
                    shape = RoundedCornerShape(100),
                    placeholder = { Text(stringResource(R.string.label_search_something)) },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = query.isNotBlank(),
                            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
                            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start)
                        ) {
                            IconButton(
                                onClick = { onQueryChanged("") }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ToggleButton(
    position: Int,
    modifier: Modifier = Modifier,
    orientation: ToggleButtonOrientation = ToggleButtonOrientation.ROW,
    startIcon: ImageVector,
    endIcon: ImageVector,
    onClicked: () -> Unit
) {
    val alpha by animateFloatAsState(targetValue = if (position == 0) 1f else 0f)

    @Composable
    fun Content() {
        Icon(
            startIcon,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = alpha))
                .padding(16.dp)
        )
        Icon(
            endIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = -alpha + 1))
                .padding(16.dp)
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = .5f),
        modifier = Modifier.then(modifier),
        shape = RoundedCornerShape(10.dp)
    ) {
        when (orientation) {
            ToggleButtonOrientation.ROW -> {
                Row(modifier = Modifier
                    .padding(6.dp)
                    .clip(RoundedCornerShape(100))
                    .clickable {
                        onClicked()
                    }
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Content()
                }
            }

            ToggleButtonOrientation.COLUMN -> {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            onClicked()
                        }
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Content()
                }
            }
        }
    }
}

enum class ToggleButtonOrientation {
    ROW,
    COLUMN
}

enum class LazyDisplayType {
    GRID,
    COLUMN
}