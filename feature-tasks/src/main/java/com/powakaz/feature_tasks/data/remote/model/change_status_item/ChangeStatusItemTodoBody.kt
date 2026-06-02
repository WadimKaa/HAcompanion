package com.powakaz.feature_tasks.data.remote.model.change_status_item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChangeStatusItemTodoBody(
    @SerialName("entity_id")
    val listId : String,
    @SerialName("item")
    val itemId : String,
    @SerialName("status")
    val status : String
)