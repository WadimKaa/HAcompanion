package com.powakaz.feature_tasks.data.remote.model.delete_item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class DeleteTodoItemRequestBody(
    @SerialName("entity_id")
    val listId : String ,
    @SerialName("item")
    val itemId : String
)