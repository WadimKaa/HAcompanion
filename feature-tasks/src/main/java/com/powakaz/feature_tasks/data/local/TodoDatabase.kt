package com.powakaz.feature_tasks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.powakaz.feature_tasks.data.local.dao.TodoDao

@Database(
    entities = [TodoItemEntity::class],
    version = 2,
    exportSchema = false
)


@TypeConverters(TodoConverters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}