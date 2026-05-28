package com.powakaz.feature_shopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddShoppingItemRequest(
    @SerialName("entity_id") val entityId: String,
    val item: String
) {

}