package com.powakaz.feature_shopping.data.mapper

import com.powakaz.feature_shopping.data.model.ShoppingItemDto
import com.powakaz.feature_shopping.domain.model.ShoppingItem

fun ShoppingItemDto.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = this.id,
        name = this.name,
        isCompleted = this.status == "completed" //если = комплит вернет тру
    )
}