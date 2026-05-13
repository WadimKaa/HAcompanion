package com.powakaz.feature_shopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoRequest(
    @SerialName("entity_id") val entityId: String
)