package com.powakaz.feature_tasks.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseDto(
    val list : List<InfoEntityDto>
)

@Serializable
data class InfoEntityDto(
    @SerialName("entity_id")
    val entityId : String,
    @SerialName("state")
    val state : Int,
    @SerialName("attributes")
    val attributesDto : AttributesDto,
    @SerialName("last_changed")
    val lastChanged : String,
    @SerialName("last_reported")
    val lastReported : String,
    @SerialName("last_updated")
    val lastUpdated : String,
    @SerialName("context")
    val contextDto : ContextDto,

    )


@Serializable
data class AttributesDto(
    @SerialName("friendly_name")
    val friendlyName : String,
    @SerialName("supported_features")
    val supportedFeatures : Int
)



@Serializable
data class ContextDto(
    @SerialName("id")
    val id : String,
    @SerialName("parent_id")
    val parentId : String?,
    @SerialName("user_id")
    val userId : String
)