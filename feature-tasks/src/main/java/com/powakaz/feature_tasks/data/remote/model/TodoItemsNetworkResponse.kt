package com.powakaz.feature_tasks.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class TodoItemsResponseDto(
    @SerialName("changed_states")
    val changedStates : List<String>,
    @SerialName("service_response")
    val serviceResponseDto : ServiceResponseDto
)


@Serializable
data class ServiceResponseDto(
    @SerialName("todo.moi_dela")
    val myTasksDto : MyTasksDto
)


@Serializable
data class MyTasksDto(
    @SerialName("items")
    val items : List<TodoItemDto>
)



@Serializable
data class TodoItemDto(
    @SerialName("uid")
    val id : String,
    @SerialName("summary")
    val title : String,
    @SerialName("status")
    val isCompleted : String
)