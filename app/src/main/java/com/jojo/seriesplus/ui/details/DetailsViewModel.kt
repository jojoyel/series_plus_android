package com.jojo.seriesplus.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.seriesplus.data.Api
import com.jojo.seriesplus.data.db.HistoryRepository
import com.jojo.seriesplus.data.models.CastResultItem
import com.jojo.seriesplus.data.models.ImagesResultItem
import com.jojo.seriesplus.data.models.SeasonsEpisodesResultItem
import com.jojo.seriesplus.data.models.SeasonsResultItem
import com.jojo.seriesplus.data.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val api: Api,
    private val historyRepository: HistoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _info = MutableStateFlow<Show?>(null)
    val info = _info.asStateFlow()

    private val _images = MutableStateFlow<List<ImagesResultItem>?>(null)
    val images = _images.asStateFlow()

    private val _cast = MutableStateFlow<List<CastResultItem>?>(null)
    val cast = _cast.asStateFlow()

    private val _seasons = MutableStateFlow<List<SeasonsResultItem>?>(null)
    val seasons = _seasons.asStateFlow()

    private val _episodes = MutableStateFlow<List<SeasonsEpisodesResultItem>?>(null)
    val episodes = _episodes.asStateFlow()

    init {
        val id = requireNotNull(savedStateHandle.get<Long>("id"))

        viewModelScope.launch(Dispatchers.IO) {
            val show = runBlocking {
                api.showDetails(id)
            }

            val imagesDeffered = async {
                val data = api.images(id).filter { !it.main }
                _images.update { data }
            }

            val castDeffered = async {
                val data = api.cast(id)
                _cast.update { data }
            }

            val seasons = async {
                val data = api.seasons(id)
                _seasons.update { data }
            }

            _info.update { show }
            historyRepository.addToHistory(show)
        }
    }

    fun onSeasonSelected(seasonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = api.seasonsEpisodes(seasonId)
            _episodes.update { data }
        }
    }
}