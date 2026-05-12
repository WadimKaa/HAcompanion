package com.powakaz.feature_tasks.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetItemsBody(
    @SerialName("entity_id")
    val entityId: String)