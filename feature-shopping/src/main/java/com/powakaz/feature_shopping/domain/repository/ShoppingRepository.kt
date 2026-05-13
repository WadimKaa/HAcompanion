package com.powakaz.feature_shopping.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.domain.model.ShoppingItem

interface ShoppingRepository {
    suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>>
}