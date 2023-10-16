package com.jojo.seriesplus.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.seriesplus.data.Api
import com.jojo.seriesplus.data.models.Person
import com.jojo.seriesplus.data.models.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val api: Api) : ViewModel() {

    private val _updatedShows = MutableStateFlow<List<Show>>(emptyList())
    val updatedShows = _updatedShows.asStateFlow()

    private val _updatedPeople = MutableStateFlow<List<Person>>(emptyList())
    val updatedPeople = _updatedPeople.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val showDeffered = async {
                val updates = api.updatesShows("day")

                updates.toList().take(15).forEach {
                    try {
                        val show = api.showDetails(it.first)

                        _updatedShows.update { list ->
                            list.toMutableList().apply { this.add(show) }.toList()
                        }
                    } catch (e: HttpException) {
                        return@async
                    }
                }
            }

            val personDeffered = async {
                val updates = api.updatesPeople("day")

                updates.toList().take(15).forEach {
                    try {
                        val person = api.person(it.first)
                        _updatedPeople.update { list ->
                            list.toMutableList().apply { this.add(person) }.toList()
                        }
                    } catch (e: HttpException) {
                        return@async
                    }
                }
            }
        }
    }
}