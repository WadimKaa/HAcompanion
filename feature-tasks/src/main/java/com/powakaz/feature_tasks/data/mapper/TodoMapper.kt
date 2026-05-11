package com.powakaz.feature_tasks.data.mapper

import com.powakaz.feature_tasks.data.remote.model.TodoItemDto
import com.powakaz.feature_tasks.domain.model.TodoItem

fun TodoItemDto.toDomain() : TodoItem{
    return TodoItem(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted == "completed"
    )
}