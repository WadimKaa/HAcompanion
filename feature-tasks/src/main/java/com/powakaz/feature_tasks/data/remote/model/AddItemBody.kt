package com.powakaz.feature_tasks.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial


@Serializable
class AddItemBody(
    @SerialName("entity_id")
    val listName : String,
    @SerialName("item")
    val itemName : String
) {
}