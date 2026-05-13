package com.powakaz.feature_shopping.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.mapper.toDomain
import com.powakaz.feature_shopping.data.model.TodoRequest
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository

class ShoppingRepositoryImpl( private val api: ShoppingApi) : ShoppingRepository {

    override suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>> {

        val result = safeApiCall {
            api.getShoppingList(TodoRequest(entityId = "todo.shopping_list"))
        }

        return when (result) {
            is NetworkResult.Success -> {
                val itemsDto = result.data.serviceResponse.todoShoppingList.items
                NetworkResult.Success(itemsDto.map { it.toDomain() })
            }
            is NetworkResult.Error -> NetworkResult.Error(result.code, result.message)
            is NetworkResult.Exception -> NetworkResult.Exception(result.e)
        }

    }
}