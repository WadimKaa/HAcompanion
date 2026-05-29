package com.powakaz.feature_tasks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_items")
data class TodoItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val isCompleted: Boolean)
