package com.jojo.seriesplus.data.db

import com.jojo.seriesplus.data.models.Person
import com.jojo.seriesplus.data.models.Show
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun addToHistory(show: Show)
    suspend fun addToHistory(person: Person)
    suspend fun removeFromHistory(id: Int)
    fun getHistory(): Flow<List<HistoryEntity>>
    suspend fun clearHistory()
}