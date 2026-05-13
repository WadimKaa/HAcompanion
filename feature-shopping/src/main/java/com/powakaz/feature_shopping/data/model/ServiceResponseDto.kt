package com.powakaz.feature_shopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponseDto(
    @SerialName("todo.shopping_list")
    val todoShoppingList : ToDoShoppingListDto
)
