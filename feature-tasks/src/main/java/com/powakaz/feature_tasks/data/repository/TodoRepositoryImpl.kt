package com.powakaz.feature_tasks.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.TodoRequest
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor (private val api : NetworkTodoListApi) : TodoRepository {

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