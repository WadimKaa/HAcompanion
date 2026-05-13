package com.powakaz.feature_shopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ToDoShoppingListDto(
    @SerialName("items")
    val items : List<ShoppingItemDto>
)
