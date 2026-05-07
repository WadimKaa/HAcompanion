package com.powakaz.feature_tasks.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.TodoItem

interface TodoRepository {
    suspend fun getTodoItems(entityId: String): NetworkResult<List<TodoItem>>
}