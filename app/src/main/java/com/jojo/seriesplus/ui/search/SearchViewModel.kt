package com.jojo.seriesplus.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.seriesplus.data.Api
import com.jojo.seriesplus.data.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: Api,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _results = MutableStateFlow<List<Show>>(emptyList())
    val results = _results.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var searchJob: Job? = Job()

    init {
        savedStateHandle.get<String>("query")?.let { q ->
            _query.update { q }
            doSearch()
        }
    }

    fun onQueryChanged(q: String) {
        val old = _query.getAndUpdate { q }

        if (old != q)
            doSearch()
    }

    private fun doSearch() {
        val q = _query.value

        if (q == "")
            return

        _isLoading.update { true }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300.milliseconds)

            val data = api.search(term = q)

            _results.update { data.map { it.show } }
            _isLoading.update { false }
        }
    }
}