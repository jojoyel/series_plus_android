package com.jojo.seriesplus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [HistoryEntity::class])
abstract class SeriesPlusDatabase : RoomDatabase() {

    abstract val historyDao: HistoryDao

    companion object {
        const val DATABASE_NAME = "series_db"
    }
}