package com.jojo.seriesplus.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.seriesplus.data.db.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepository: HistoryRepository) :
    ViewModel() {

    val history =
        historyRepository.getHistory().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onClearItem(id: Int?) {
        id?.let {
            viewModelScope.launch {
                historyRepository.removeFromHistory(it)
            }
        }
    }
}