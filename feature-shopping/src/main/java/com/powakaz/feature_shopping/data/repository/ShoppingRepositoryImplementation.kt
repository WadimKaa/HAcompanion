package com.powakaz.feature_shopping.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.mapper.toDomain
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository

class ShoppingRepositoryImplementation(private val api: ShoppingApi) : ShoppingRepository {

    override suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>> {
        return try {

            val response = api.getShoppingList() //запрос

            val domainItems = response.map { it.toDomain() } //маппим список дто в домайн

            NetworkResult.Success(domainItems)

        } catch (e: Exception) {
            NetworkResult.Exception(e)
        }
    }
}