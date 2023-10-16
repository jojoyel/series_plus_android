package com.jojo.seriesplus.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val dataId: Long,
    val title: String,
    val url: String?,
    val type: HistoryType
)

enum class HistoryType {
    SHOW, PERSON
}