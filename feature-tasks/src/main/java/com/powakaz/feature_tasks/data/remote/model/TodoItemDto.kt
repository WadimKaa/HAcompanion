package com.powakaz.feature_tasks.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    @SerialName("uid")
    val id : String,
    @SerialName("summary")
    val title : String,
    @SerialName("status")
    val isCompleted : String
)