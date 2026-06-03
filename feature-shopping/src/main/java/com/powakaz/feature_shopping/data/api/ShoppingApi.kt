package com.powakaz.feature_shopping.data.api

import com.powakaz.feature_shopping.data.model.AddShoppingItemRequest
import com.powakaz.feature_shopping.data.model.DeleteItemRequest
import com.powakaz.feature_shopping.data.model.ServiceResponseDto
import com.powakaz.feature_shopping.data.model.ShoppingItemDto
import com.powakaz.feature_shopping.data.model.ShoppingItemsResponseDto
import com.powakaz.feature_shopping.data.model.TodoRequest
import com.powakaz.feature_shopping.data.model.UpdateItemRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShoppingApi {

    @POST("api/services/todo/get_items?return_response")
    suspend fun getShoppingList(
        @Body request: TodoRequest
    ): ShoppingItemsResponseDto

    @POST("api/services/todo/add_item")
    suspend fun addShoppingItem(
        @Body request: AddShoppingItemRequest
    )

    @POST("api/services/todo/update_item")
    suspend fun updateShoppingItem(
        @Body request: UpdateItemRequest
    )

    @POST("api/services/todo/remove_item")
    suspend fun deleteShoppingItem(
        @Body request: DeleteItemRequest
    )
}