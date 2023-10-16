package com.jojo.seriesplus.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jojo.seriesplus.data.models.Show

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultItem(
    modifier: Modifier = Modifier,
    data: Show,
    displayType: CardDisplayType = CardDisplayType.ROW,
    onClicked: () -> Unit
) {
    when (displayType) {
        CardDisplayType.ROW -> {
            Card(onClick = onClicked, modifier = Modifier.then(modifier)) {
                Surface(color = MaterialTheme.colorScheme.secondaryContainer) {
                    Row(Modifier.fillMaxWidth()) {
                        DistantImage(
                            url = data.image?.medium ?: data.image?.original,
                            elevation = 4.dp,
                            modifier = Modifier
                                .height(152.dp)
                                .aspectRatio(1f / 1.25f),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp, 4.dp)
                        ) {
                            Text(
                                text = data.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 2
                            )
                            Genres(data.genres)
                        }
                    }
                }
            }
        }

        CardDisplayType.SQUARE -> {
            Card(onClick = onClicked, modifier = Modifier.then(modifier)) {
                Surface(color = MaterialTheme.colorScheme.secondaryContainer) {
                    Column(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DistantImage(
                            url = data.image?.medium ?: data.image?.original,
                            modifier = Modifier
                                .height(182.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .aspectRatio(1f / 1.25f),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = data.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Genres(data.genres)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Genres(genres: List<String>, limit: Int = 3) {
    Text(
        text = genres.take(limit).joinToString().ifBlank { "" },
        fontStyle = FontStyle.Italic,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

enum class CardDisplayType {
    ROW,
    SQUARE
}