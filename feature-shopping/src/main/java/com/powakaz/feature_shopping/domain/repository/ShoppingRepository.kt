package com.powakaz.feature_shopping.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.data.model.ShoppingItemDto
import com.powakaz.feature_shopping.domain.model.ShoppingItem

interface ShoppingRepository {
    suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>>
    suspend fun addShoppingItem(name: String): NetworkResult<Unit>
    suspend fun updateShoppingItem(itemId: String, isCompleted: Boolean) : NetworkResult<Unit>
    suspend fun deleteShoppingItem(itemId: String) : NetworkResult<Unit>

}