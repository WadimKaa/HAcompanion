package com.powakaz.feature_tasks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.powakaz.feature_tasks.data.local.dao.TodoDao

@Database(
    entities = [TodoItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}