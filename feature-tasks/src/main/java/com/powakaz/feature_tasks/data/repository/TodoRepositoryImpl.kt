package com.powakaz.feature_tasks.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.mapper.toDomain
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.GetItemsBody
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val api: NetworkTodoListApi) : TodoRepository {

    override suspend fun getTodoItems(listName: String): NetworkResult<List<TodoItem>> {

        return safeApiCall {
            val response = api.getTodoItems(GetItemsBody(listName))

            response.serviceResponseDto.myTasksDto.items.map {
                it.toDomain()
            }
        }

    }


    override suspend fun addTodoItem(entityName: String, listName: String) : NetworkResult<Response> {
        return safeApiCall {
            val response = api.addTodoItem(AddItemBody(entityName, listName))

        }
    }
}