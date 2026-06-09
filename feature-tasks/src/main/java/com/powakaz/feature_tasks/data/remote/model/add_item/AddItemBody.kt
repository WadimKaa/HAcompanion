package com.powakaz.feature_tasks.data.remote.model.add_item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddItemBody(
    @SerialName("entity_id")
    val listName : String,
    @SerialName("item")
    val itemName : String
) {
}