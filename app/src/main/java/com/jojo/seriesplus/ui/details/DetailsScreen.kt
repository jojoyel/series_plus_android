@file:OptIn(ExperimentalMaterial3Api::class)

package com.jojo.seriesplus.ui.details

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jojo.seriesplus.R
import com.jojo.seriesplus.data.models.CastResultItem
import com.jojo.seriesplus.data.models.ImagesResultItem
import com.jojo.seriesplus.data.models.SeasonsEpisodesResultItem
import com.jojo.seriesplus.data.models.SeasonsResultItem
import com.jojo.seriesplus.data.models.Show
import com.jojo.seriesplus.ui.composables.ContentBox
import com.jojo.seriesplus.ui.composables.DistantImage
import com.jojo.seriesplus.ui.composables.Genres
import com.jojo.seriesplus.ui.composables.ImageVisualizer
import com.jojo.seriesplus.ui.composables.TextIcon
import com.jojo.seriesplus.ui.theme.SeriesPlusTheme
import com.jojo.seriesplus.util.Routes
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Composable
fun DetailsScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<DetailsViewModel>()

    val info by viewModel.info.collectAsState()
    val images by viewModel.images.collectAsState()
    val cast by viewModel.cast.collectAsState()
    val seasons by viewModel.seasons.collectAsState()
    val episodes by viewModel.episodes.collectAsState()

    var selectedSeason by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var selectedCast by remember { mutableStateOf<CastResultItem?>(null) }
    var showImage by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current

    val scrollState = rememberScrollState()
    var mainColumnSize by remember { mutableStateOf(IntSize.Zero) }

    BackHandler(enabled = showImage != null) {}

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { mainColumnSize = it }
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy((-40).dp)
        ) {
            info?.let { show ->
                if (show.image != null)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(with(LocalDensity.current) { mainColumnSize.height.toDp() } * .65f)
                            .background(MaterialTheme.colorScheme.surface)
                            .graphicsLayer {
                                scaleX = min(
                                    1f +
                                            scrollState.value.toFloat() / max(
                                        scrollState.maxValue,
                                        1
                                    ) * .2f, 1.1f
                                )
                                scaleY = min(
                                    1f +
                                            scrollState.value.toFloat() / max(
                                        scrollState.maxValue,
                                        1
                                    ) * .2f, 1.1f
                                )
                                alpha =
                                    max(
                                        1f - ((scrollState.value.toFloat() / max(
                                            scrollState.maxValue,
                                            1
                                        )) * 1.5f), .2f
                                    )
                                translationY = 0.5f * scrollState.value
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        DistantImage(
                            url = show.image.original,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RectangleShape)
                        )
                    }
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            Modifier
                                .padding(14.dp)
                                .fillMaxWidth(.85f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = show.name,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            if (show.ended != null)
                                Text(
                                    stringResource(R.string.show_ended),
                                    style = MaterialTheme.typography.labelLarge
                                )
                        }

                        MainInfo(show) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }

                        ImagesRow(
                            modifier = Modifier.fillMaxWidth(),
                            images = images
                        ) { showImage = it }

                        show.summary?.let { sum ->
                            Summary(paragraph = sum)
                        }

                        SeasonsRow(seasons = seasons) {
                            viewModel.onSeasonSelected(it.first)
                            selectedSeason = Pair(it.first, it.second)
                        }

                        CastRow(cast = cast) { id ->
                            cast?.let {
                                selectedCast = it.first { c -> c.person.id == id }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            images != null && images!!.isNotEmpty() && showImage != null,
            enter = fadeIn(initialAlpha = .9f),
            exit = fadeOut()
        ) {
            images?.let { i ->
                showImage?.let { index ->
                    ImageVisualizer(
                        url = i.map { it.resolutions.original.url },
                        index = index,
                        animatedVisibilityScope = this
                    ) {
                        showImage = null
                    }
                }
            }
        }

        if (info == null)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize(.1f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.loading_info))
            }
    }

    seasons?.let {
        SeasonsBottomSheet(
            selectedSeason,
            seasons = it,
            episodes = episodes,
            seasonClicked = { id, name ->
                viewModel.onSeasonSelected(id)
                selectedSeason = Pair(id, name)
            }
        ) { selectedSeason = null }
    }

    cast?.let {
        CastBottomSheet(
            selectedCast,
            onPersonClicked = { navController.navigate("${Routes.Actor.route}/$it") }) {
            selectedCast = null
        }
    }
}

@Composable
fun MainInfo(info: Show, modifier: Modifier = Modifier, onLinkClicked: (String) -> Unit) {
    ContentBox(color = MaterialTheme.colorScheme.surfaceVariant) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.genres))
                Genres(genres = info.genres)
            }

            Text(stringResource(R.string.original_lang, info.language))

            info.rating.average?.let {
                Column(
                    Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.rating))
                    Spacer(Modifier.height(4.dp))
                    RatingBar(value = it, height = 40.dp)
                }
            }

            Column {
                Text(stringResource(R.string.links))
                TextButton(onClick = { onLinkClicked(info.url) }) {
                    Text(text = info.url, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                info.officialSite?.let {
                    TextButton(onClick = { onLinkClicked(it) }) {
                        Text(text = it, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 12.dp, top = 4.dp)
    )
}

@Composable
fun RatingBar(modifier: Modifier = Modifier, height: Dp, value: Double) {
    assert(value in 0.0..10.0)

    val backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.Gray
    val foregroundColor = MaterialTheme.colorScheme.primary

    val numberToDisplay = floor(value / 2).toInt()
    val decimalValue = value / 2 - numberToDisplay

    val offsets = listOf(.24f, .37f, .5f, .63f, .76f)

    val animatedFloats =
        List(if (decimalValue > 0f) numberToDisplay + 1 else numberToDisplay) {
            remember { Animatable(0f) }
        }

    LaunchedEffect(numberToDisplay) {
        animatedFloats.forEachIndexed { index, it ->
            when (index) {
                numberToDisplay -> {
                    it.animateTo(-decimalValue.toFloat(), initialVelocity = .5f)
                }

                else -> {
                    it.animateTo(-1f, initialVelocity = .5f)
                }
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .then(modifier)
    ) {
        val radius = size.height * .4f
        drawCircle(
            backgroundColor,
            radius = radius,
            center = Offset(x = size.width * .24f, y = size.height / 2f)
        )
        drawCircle(
            backgroundColor,
            radius = radius,
            center = Offset(x = size.width * .37f, y = size.height / 2f)
        )
        drawCircle(
            backgroundColor,
            radius = radius,
            center = Offset(x = size.width * .5f, y = size.height / 2f)
        )
        drawCircle(
            backgroundColor,
            radius = radius,
            center = Offset(x = size.width * .63f, y = size.height / 2f)
        )
        drawCircle(
            backgroundColor,
            radius = radius,
            center = Offset(x = size.width * .76f, y = size.height / 2f)
        )

        animatedFloats.forEachIndexed { index, it ->
            when (index) {
                numberToDisplay -> {
                    drawArc(
                        color = foregroundColor,
                        startAngle = 270f,
                        sweepAngle = 360f * it.value,
                        useCenter = true,
                        size = Size(size.height * .8f, size.height * .8f),
                        topLeft = Offset(
                            x = (size.width * offsets[index]) - (size.height * .8f) / 2,
                            y = size.height * .1f
                        )
                    )
                }

                else -> drawArc(
                    color = foregroundColor,
                    startAngle = 270f,
                    sweepAngle = 360f * it.value,
                    useCenter = true,
                    size = Size(size.height * .8f, size.height * .8f),
                    topLeft = Offset(
                        x = (size.width * offsets[index]) - (size.height * .8f) / 2,
                        y = size.height * .1f
                    )
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE
)
@Composable
fun RatingBarPreview() {
    SeriesPlusTheme {
        Column {
            RatingBar(value = 4.3, height = 50.dp)
            Spacer(modifier = Modifier.height(50.dp))
            RatingBar(value = 6.0, height = 50.dp)
        }
    }
}

@Composable
private fun ImagesRow(
    modifier: Modifier = Modifier,
    images: List<ImagesResultItem>?,
    onImageClicked: (Int) -> Unit
) {
    ContentBox {
        Column(Modifier.padding(8.dp)) {
            SectionTitle(stringResource(R.string.title_images))
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .then(modifier), contentAlignment = Alignment.Center
            ) {
                images?.let { images ->
                    if (images.isEmpty())
                        Text(stringResource(R.string.images_no_data))
                    else {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.clip(RoundedCornerShape(18.dp))
                        ) {
                            itemsIndexed(images) { index, it ->
                                DistantImage(
                                    url = it.resolutions.medium?.url ?: it.resolutions.original.url,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .widthIn(max = 250.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onImageClicked(index) },
                                )
                            }
                        }
                    }
                } ?: run {
                    CircularProgressIndicator(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun SeasonsRow(seasons: List<SeasonsResultItem>?, onClick: (Pair<Int, String>) -> Unit) {
    ContentBox {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            SectionTitle(stringResource(R.string.title_seasons))
            Spacer(Modifier.height(8.dp))
            Box(contentAlignment = Alignment.Center) {
                seasons?.let {
                    if (seasons.isEmpty())
                        Text(stringResource(R.string.no_data), Modifier.padding(12.dp))
                    else
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(seasons) {
                                val string = stringResource(
                                    R.string.season_number,
                                    it.number
                                )
                                SeasonCard(info = it) {
                                    onClick(Pair(it.id, it.name.ifBlank { string }))
                                }
                            }
                        }
                } ?: run {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun CastRow(modifier: Modifier = Modifier, cast: List<CastResultItem>?, onClick: (Long) -> Unit) {
    ContentBox {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            SectionTitle(stringResource(R.string.title_casting))
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.then(modifier), contentAlignment = Alignment.Center) {
                cast?.let {
                    if (cast.isEmpty())
                        Text(stringResource(R.string.no_data))
                    else {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(cast) {
                                CastCard(info = it, modifier = Modifier) {
                                    onClick(it.person.id)
                                }
                            }
                        }
                    }
                } ?: run {
                    CircularProgressIndicator(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun SeasonCard(info: SeasonsResultItem, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier) {
        Column(Modifier.width(142.dp)) {
            DistantImage(
                url = info.image?.medium ?: info.image?.original,
                modifier = Modifier.height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(6.dp)) {
                val string = stringResource(R.string.season_number, info.number)
                Text(
                    text = info.name.ifBlank { string },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Text(stringResource(R.string.episodes_number, info.episodeOrder))
            }
        }
    }
}

@Composable
fun CastCard(modifier: Modifier = Modifier, info: CastResultItem, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = Modifier
            .width(150.dp)
            .then(modifier)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            DistantImage(
                url = info.person.image?.medium ?: info.person.image?.original
                ?: info.character.image?.medium ?: info.character.image?.original,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = info.person.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                text = info.character.name,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun Summary(paragraph: String) {
    val spannableString = SpannableStringBuilder(paragraph).toString()
    val spanned = HtmlCompat.fromHtml(spannableString, HtmlCompat.FROM_HTML_MODE_COMPACT)

    var expandable by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

    ContentBox(Modifier.clickable(expandable) { expanded = !expanded }) {
        Column(Modifier.padding(8.dp)) {
            SectionTitle(stringResource(R.string.title_resume))
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = spanned.toAnnotatedString(),
                Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .animateContentSize(),
                maxLines = if (!expanded) 5 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayout ->
                    if (!expanded)
                        expandable = textLayout.isLineEllipsized(textLayout.lineCount - 1)
                }
            )

            if (expandable)
                TextButton(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary)
                ) {
                    Text(text = stringResource(if (expanded) R.string.action_less else R.string.action_plus))
                }
        }
    }
}

// https://stackoverflow.com/questions/66494838/android-compose-how-to-use-html-tags-in-a-text-view#answer-68935732
fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ), start, end
                )
            }

            is UnderlineSpan -> addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline),
                start,
                end
            )

            is ForegroundColorSpan -> addStyle(
                SpanStyle(color = Color(span.foregroundColor)),
                start,
                end
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeasonsBottomSheet(
    selectedSeason: Pair<Int, String>?,
    seasons: List<SeasonsResultItem>,
    episodes: List<SeasonsEpisodesResultItem>?,
    seasonClicked: (Int, String) -> Unit,
    onClose: () -> Unit
) {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    if (selectedSeason != null) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = bottomSheetState
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(Modifier.fillMaxWidth()) {
                    SeasonsMenu(currentSeasonName = selectedSeason.second, seasons = seasons.map {
                        Pair(
                            it.id,
                            it.name.ifBlank { "Saison ${it.number}" })
                    }, seasonClicked = seasonClicked)
                }
                Divider(Modifier.fillMaxWidth(.8f))

                if (episodes != null)
                    LazyColumn(
                        Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(episodes, key = { it.id }) {
                            EpisodeCard(modifier = Modifier.animateItemPlacement(), episode = it)
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EpisodeCard(modifier: Modifier = Modifier, episode: SeasonsEpisodesResultItem) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded }, modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .then(modifier)
    ) {
        Column() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(.35f)) {
                    DistantImage(
                        url = episode.image?.medium ?: episode.image?.original,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Column(
                    Modifier
                        .weight(.65f)
                        .padding(4.dp)
                ) {
                    AnimatedContent(
                        targetState = if (!expanded) episode.name else "",
                        transitionSpec = {
                            fadeIn() + slideInVertically { it / 2 } with fadeOut() + slideOutVertically { it / 2 }
                        },
                        label = "episode-name"
                    ) {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    TextIcon(
                        text = stringResource(R.string.minutes_duration, episode.runtime),
                        icon = Icons.Default.Timelapse,
                        contentDescription = null
                    )
                    TextIcon(
                        text = episode.airdate,
                        icon = Icons.Default.CalendarMonth,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                val spannableString = SpannableStringBuilder(episode.summary ?: "").toString()
                val spanned =
                    HtmlCompat.fromHtml(spannableString, HtmlCompat.FROM_HTML_MODE_COMPACT)

                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = episode.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = spanned.toAnnotatedString())

                    episode.rating.average?.let {
                        Row {
                            Text(stringResource(R.string.rating))
                            RatingBar(height = 20.dp, value = it)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SeasonsMenu(
    currentSeasonName: String = "",
    seasons: List<Pair<Int, String>>,
    seasonClicked: (Int, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) -180f else 0f,
        label = "icon-rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Row(
            Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(6.dp))
                .clickable { expanded = !expanded }, verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(currentSeasonName, label = "animated-season-name") {
                Text(text = it, Modifier.padding(vertical = 16.dp, horizontal = 6.dp))
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            seasons.let {
                it.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.second) },
                        onClick = {
                            seasonClicked(s.first, s.second)
                            expanded = false
                        })
                }
            }
        }
    }
}

@Composable
fun CastBottomSheet(cast: CastResultItem?, onPersonClicked: (Long) -> Unit, onClose: () -> Unit) {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    if (cast != null) {
        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = bottomSheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(.5f)) {
                        DistantImage(
                            url = cast.person.image?.medium ?: cast.person.image?.original,
                            modifier = Modifier
                                .height(150.dp)
                                .clip(RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp))
                        )
                        Text(cast.person.name, Modifier.padding(end = 4.dp))
                    }
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(.5f)) {
                        DistantImage(
                            url = cast.character.image?.medium ?: cast.character.image?.original,
                            modifier = Modifier
                                .height(150.dp)
                                .clip(RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp))
                        )
                        Text(cast.character.name, Modifier.padding(start = 4.dp))
                    }
                }
                OutlinedButton(onClick = { onPersonClicked(cast.person.id) }) {
                    Icon(Icons.Default.OpenInNew, contentDescription = null)
                    Text(stringResource(R.string.action_more_info))
                }
            }
        }
    }
}