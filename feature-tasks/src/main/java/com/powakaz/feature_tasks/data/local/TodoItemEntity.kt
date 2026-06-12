package com.powakaz.feature_tasks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


enum class SyncStatus {
    SYNCED,
    PENDING_INSERT,
    PENDING_GET_ID,
    PENDING_CHANGE_STATUS,
    PENDING_CHANGE_NAME,
    PENDING_DELETE
}


class TodoConverters{

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus) : String = status.name

    @TypeConverter
    fun toSyncStatus(value : String) : SyncStatus = SyncStatus.valueOf(value)
}


@Entity(tableName = "todo_items")
data class TodoItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val isCompleted: Boolean,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val serverId: String? = null
)
