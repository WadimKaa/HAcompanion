package com.powakaz.feature_tasks.data.remote.model.update_todo_item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpdateTodoItemBody(
    @SerialName("entity_id")
    val listId : String,
    @SerialName("item")
    val entityId : String,
    @SerialName("rename")
    val entityName : String
)
