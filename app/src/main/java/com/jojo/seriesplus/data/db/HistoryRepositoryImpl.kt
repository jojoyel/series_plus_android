package com.jojo.seriesplus.data.db

import com.jojo.seriesplus.data.models.Person
import com.jojo.seriesplus.data.models.Show
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(private val dao: HistoryDao) : HistoryRepository {
    override suspend fun addToHistory(show: Show) {
        val last = dao.getLastItem()
        if (last != null && last.dataId == show.id && last.type == HistoryType.SHOW)
            return

        val entity =
            HistoryEntity(
                dataId = show.id,
                title = show.name,
                url = show.image?.medium ?: show.image?.original,
                type = HistoryType.SHOW
            )

        dao.addItem(entity)
    }

    override suspend fun addToHistory(person: Person) {
        val last = dao.getLastItem()
        if (last != null && last.dataId == person.id && last.type == HistoryType.PERSON)
            return

        val entity = HistoryEntity(
            dataId = person.id,
            title = person.name,
            url = person.image?.medium ?: person.image?.original,
            type = HistoryType.PERSON
        )

        dao.addItem(entity)
    }

    override suspend fun removeFromHistory(id: Int) {
        dao.removeItem(id)
    }

    override fun getHistory(): Flow<List<HistoryEntity>> {
        return dao.getHistory()
    }

    override suspend fun clearHistory() {
        dao.clearTable()
    }
}