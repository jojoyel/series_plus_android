package com.jojo.seriesplus.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jojo.seriesplus.R
import com.jojo.seriesplus.data.models.Person
import com.jojo.seriesplus.ui.composables.CardDisplayType
import com.jojo.seriesplus.ui.composables.DistantImage
import com.jojo.seriesplus.ui.composables.ResultItem
import com.jojo.seriesplus.ui.details.SectionTitle
import com.jojo.seriesplus.util.Routes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val updatedShows by viewModel.updatedShows.collectAsState()
    val updatedPeople by viewModel.updatedPeople.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
        ) {
            Text(
                text = stringResource(R.string.welcome),
                Modifier.padding(top = 24.dp, start = 36.dp, bottom = 24.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            Column {
                SectionTitle(stringResource(R.string.home_shows_updated))
                LazyRow(
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(updatedShows, key = { it.id }) {
                        ResultItem(
                            data = it,
                            displayType = CardDisplayType.SQUARE,
                            modifier = Modifier
                                .width(170.dp)
                                .animateItemPlacement()
                        ) {
                            navController.navigate("${Routes.Details.route}/${it.id}")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                SectionTitle(stringResource(R.string.home_people_updated))
                LazyRow(
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(updatedPeople, key = { it.id }) {
                        PersonCard(p = it, modifier = Modifier.animateItemPlacement()) {
                            navController.navigate("${Routes.Actor.route}/${it.id}")
                        }
                    }
                }
            }
        }
        ClickableOutlinedTextField(Modifier.padding(8.dp)) { navController.navigate(Routes.Search.route) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonCard(modifier: Modifier = Modifier, p: Person, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = Modifier
            .width(150.dp)
            .then(modifier)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            DistantImage(
                url = p.image?.medium ?: p.image?.original,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = p.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun ClickableOutlinedTextField(
    modifier: Modifier = Modifier,
    clicked: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .clip(RoundedCornerShape(100))
            .clickable { clicked() }
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .border(
                    2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(100)
                )
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.label_search_something),
                modifier = Modifier.weight(1f)
            )
        }
    }
}