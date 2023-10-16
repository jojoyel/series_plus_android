package com.jojo.seriesplus.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jojo.seriesplus.R
import com.jojo.seriesplus.data.db.HistoryEntity
import com.jojo.seriesplus.data.db.HistoryType
import com.jojo.seriesplus.ui.composables.DistantImage
import com.jojo.seriesplus.util.Routes

@Composable
fun HistoryScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<HistoryViewModel>()

    val history by viewModel.history.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.history),
            Modifier.padding(top = 24.dp, start = 36.dp, bottom = 24.dp),
            style = MaterialTheme.typography.headlineLarge
        )
        if (history.isNotEmpty())
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history.asReversed(), key = { it.id!! }) {
                    HistoryItem(
                        h = it,
                        onDeleteClicked = { viewModel.onClearItem(it.id) }) {
                        when (it.type) {
                            HistoryType.SHOW -> navController.navigate("${Routes.Details.route}/${it.dataId}")
                            HistoryType.PERSON -> navController.navigate("${Routes.Actor.route}/${it.dataId}")
                        }
                    }
                }
            }
        else
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.HistoryToggleOff,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    stringResource(R.string.no_history),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    h: HistoryEntity,
    onDeleteClicked: () -> Unit,
    onClicked: () -> Unit
) {
    var deleteEnabled by remember { mutableStateOf(true) }

    Card(onClick = onClicked, modifier = Modifier.then(modifier)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f)) {
                DistantImage(url = h.url, modifier = Modifier.height(120.dp))
                Text(
                    h.title,
                    fontStyle = MaterialTheme.typography.displayLarge.fontStyle,
                    modifier = Modifier.padding(6.dp)
                )
            }
            IconButton(onClick = {
                deleteEnabled = false
                onDeleteClicked()
            }, enabled = deleteEnabled) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }
    }
}