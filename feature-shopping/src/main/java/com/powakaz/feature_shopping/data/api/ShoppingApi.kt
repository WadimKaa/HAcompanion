package com.powakaz.feature_shopping.data.api

import com.powakaz.feature_shopping.data.model.ServiceResponseDto
import com.powakaz.feature_shopping.data.model.ShoppingItemDto
import com.powakaz.feature_shopping.data.model.ShoppingItemsResponseDto
import com.powakaz.feature_shopping.data.model.TodoRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShoppingApi {

    @POST("api/services/todo/get_items?return_response")
    suspend fun getShoppingList(
        @Body request: TodoRequest
    ): ShoppingItemsResponseDto

}