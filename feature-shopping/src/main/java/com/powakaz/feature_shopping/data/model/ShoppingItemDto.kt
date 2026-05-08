package com.powakaz.feature_shopping.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ShoppingItemDto(
    @SerialName("uid") val id: String,
    @SerialName("summary") val name: String,
    @SerialName("status") val status: String
)
