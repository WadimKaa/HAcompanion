package com.powakaz.feature_shopping.data.model

import android.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UpdateItemRequest(
    @SerialName("entity_id")
    val entityId : String,
    val item: String,
    val status: String

) {
}