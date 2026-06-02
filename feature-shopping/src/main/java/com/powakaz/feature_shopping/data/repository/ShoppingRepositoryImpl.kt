package com.powakaz.feature_shopping.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.mapper.toDomain
import com.powakaz.feature_shopping.data.model.AddShoppingItemRequest
import com.powakaz.feature_shopping.data.model.TodoRequest
import com.powakaz.feature_shopping.data.model.UpdateItemRequest
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository


class ShoppingRepositoryImpl( private val api: ShoppingApi) : ShoppingRepository {

    private val entityId  = "todo.shopping_list"

    override suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>> {

        val result = safeApiCall {
            api.getShoppingList(TodoRequest(entityId = entityId))
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

    override suspend fun addShoppingItem(name: String): NetworkResult<Unit> {
        return safeApiCall {
            api.addShoppingItem(
                AddShoppingItemRequest(
                    entityId = entityId,
                    item = name
                )
            )
        }
    }

    override suspend fun updateShoppingItem(
        itemId: String,
        isCompleted: Boolean
    ): NetworkResult<Unit> {
        val status = if (isCompleted) "completed" else "needs_action"

        return safeApiCall {
            api.updateShoppingItem(
                UpdateItemRequest(
                    entityId = entityId,
                    item = itemId,
                    status = status
                )
            )
        }

    }
}