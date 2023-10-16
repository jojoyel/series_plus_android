package com.jojo.seriesplus.ui.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jojo.seriesplus.R
import com.jojo.seriesplus.ui.composables.CardDisplayType
import com.jojo.seriesplus.ui.composables.ContentBox
import com.jojo.seriesplus.ui.composables.DistantImage
import com.jojo.seriesplus.ui.composables.ResultItem
import com.jojo.seriesplus.ui.composables.TextIcon
import com.jojo.seriesplus.ui.details.SectionTitle
import com.jojo.seriesplus.util.Routes
import kotlin.math.max

@Composable
fun ActorScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<ActorViewModel>()

    val actor by viewModel.actor.collectAsState()
    val characters by viewModel.characters.collectAsState()

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy((-60).dp)
        ) {
            actor?.let { actor ->
                Column {
                    Row(
                        Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(
                                    alpha = max(
                                        1f - ((scrollState.value.toFloat() / max(
                                            scrollState.maxValue,
                                            1
                                        )) * .5f), .5f
                                    )
                                )
                            )
                            .height(250.dp)
                            .padding(horizontal = 12.dp), verticalAlignment = Alignment.Bottom
                    ) {
                        Box(modifier = Modifier) {
                            DistantImage(
                                url = actor.image?.medium ?: actor.image?.original,
                                modifier = Modifier
                                    .height(204.dp)
                                    .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                            )
                        }
                        Row(
                            Modifier
                                .clip(RoundedCornerShape(topEnd = 15.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(14.dp)
                                .weight(1f),
                            horizontalArrangement = Arrangement.Center,

                            ) {
                            Text(
                                text = actor.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            ContentBox(modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(8.dp)) {
                                    actor.birthday?.let { birthday ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            TextIcon(
                                                text = birthday,
                                                icon = Icons.Default.Cake,
                                                contentDescription = null
                                            )
                                            actor.deathday?.let {
                                                Text(text = "- $it")
                                            }
                                        }
                                    }
                                    TextIcon(
                                        text = stringResource(if (actor.gender == "Male") R.string.gender_male else R.string.gender_female),
                                        icon = if (actor.gender == "Male") Icons.Default.Male else Icons.Default.Female,
                                        contentDescription = null
                                    )
                                }
                            }

                            ContentBox(
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 550.dp)
                            ) {
                                Column(Modifier.padding(8.dp)) {
                                    SectionTitle(
                                        text = stringResource(
                                            R.string.find_actor_in,
                                            actor.name
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    characters?.let {
                                        LazyVerticalGrid(
                                            columns = GridCells.Fixed(2),
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            items(it) {
                                                ResultItem(
                                                    data = it.embedded.show,
                                                    displayType = CardDisplayType.SQUARE
                                                ) {
                                                    navController.navigate("${Routes.Details.route}/${it.embedded.show.id}")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (actor == null)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize(.1f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.loading_info))
            }
    }
}