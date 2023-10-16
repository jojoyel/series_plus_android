package com.jojo.seriesplus.ui.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.seriesplus.data.Api
import com.jojo.seriesplus.data.db.HistoryRepository
import com.jojo.seriesplus.data.models.Person
import com.jojo.seriesplus.data.models.PersonCastResultItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    private val api: Api,
    private val historyRepository: HistoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _actor = MutableStateFlow<Person?>(null)
    val actor = _actor.asStateFlow()

    private val _characters = MutableStateFlow<List<PersonCastResultItem>?>(null)
    val characters = _characters.asStateFlow()

    init {
        savedStateHandle.get<Long>("id")?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val person = api.person(id)

                val defferedShow = async {
                    val data = api.personCast(id)
                    _characters.update { data }
                }

                _actor.update { person }
                historyRepository.addToHistory(person)
            }
        }
    }
}