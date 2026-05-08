package com.powakaz.feature_shopping.data.api

import com.powakaz.feature_shopping.data.model.ShoppingItemDto
import retrofit2.http.GET

interface ShoppingApi {

    @GET("api/services/todo/get_items?return_response")
    suspend fun getShoppingList(): List<ShoppingItemDto>

}