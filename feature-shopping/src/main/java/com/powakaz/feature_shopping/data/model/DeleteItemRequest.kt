package com.powakaz.feature_shopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteItemRequest(
    @SerialName("entity_id") val entityId: String,
    val item: String
) {
}