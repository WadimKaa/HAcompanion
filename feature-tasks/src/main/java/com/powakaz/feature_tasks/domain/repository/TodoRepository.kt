package com.powakaz.feature_tasks.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem

interface TodoRepository {
    suspend fun getTodoItems(listName: String): NetworkResult<List<TodoItem>>
    suspend fun addTodoItem(entityName: String, listName : String)
}