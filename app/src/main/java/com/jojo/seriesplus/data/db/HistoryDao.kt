package com.jojo.seriesplus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history ORDER BY id DESC LIMIT 1")
    fun getLastItem(): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(entity: HistoryEntity)

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun removeItem(id: Int)

    @Query("DELETE FROM history")
    suspend fun clearTable()
}