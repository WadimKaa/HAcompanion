package com.powakaz.feature_tasks.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.remote.HomeAssistantToDoListApi
import com.powakaz.feature_tasks.data.remote.model.TodoRequest
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository

class TodoRepositoryImpl (private val api : HomeAssistantToDoListApi) : TodoRepository {

    override suspend fun getTodoItems(entityId: String): NetworkResult<List<TodoItem>> {

        return safeApiCall {
            val response = api.getTodoItems(TodoRequest(entityId))


            response.map { dto ->
                TodoItem(
                    id = dto.id,
                    title = dto.title,
                    isCompleted = dto.isCompleted
                )
            }
        }

    }
}