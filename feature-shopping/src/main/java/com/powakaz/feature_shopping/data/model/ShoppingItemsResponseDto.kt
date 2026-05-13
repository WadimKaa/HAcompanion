package com.powakaz.feature_shopping.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItemsResponseDto(
    @SerialName("changed_states")
    val array : List<String>,
    @SerialName("service_response")
    val serviceResponse : ServiceResponseDto

)
