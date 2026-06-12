package com.powakaz.feature_tasks.data.mapper

import com.powakaz.feature_tasks.data.local.TodoItemEntity
import com.powakaz.feature_tasks.data.remote.model.add_item.InfoEntityDto
import com.powakaz.feature_tasks.data.remote.model.get_items.TodoItemDto
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem
import java.util.UUID

fun TodoItemDto.toDomain(): TodoItem {
    return TodoItem(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted == "completed"
    )
}


fun List<InfoEntityDto>.toDomain(): Response {
    return Response(
        isSuccess = true
    )
}

fun TodoItemDto.toEntity(): TodoItemEntity {
    return TodoItemEntity(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted == "completed"
    )
}

fun TodoItemEntity.toDomain(): TodoItem {
    return TodoItem(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted
    )
}
