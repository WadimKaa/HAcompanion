package com.powakaz.feature_tasks.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodoItem(id : String): TodoItem
    suspend fun addTodoItem(entityName: String, listName: String): NetworkResult<Response>
    suspend fun deleteTodoItem(entityId: String): NetworkResult<Response>
    suspend fun changeStateTodoItem(entityId: String, isCompleted : Boolean): NetworkResult<Response>
    suspend fun renameTodoItem(todoItem: TodoItem): NetworkResult<Response>


    fun observeTodoItems(): Flow<List<TodoItem>>
    fun observeTodoItem(id: String): Flow<TodoItem?>
    suspend fun refreshTodoItems(listName: String) : Result<Unit>

}