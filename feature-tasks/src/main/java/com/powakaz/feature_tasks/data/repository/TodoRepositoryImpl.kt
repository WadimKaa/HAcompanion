package com.powakaz.feature_tasks.data.repository

import android.util.Log
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.local.dao.TodoDao
import com.powakaz.feature_tasks.data.mapper.toDomain
import com.powakaz.feature_tasks.data.mapper.toEntity
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.add_item.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.delete_item.DeleteTodoItemRequestBody
import com.powakaz.feature_tasks.data.remote.model.get_items.GetItemsBody
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val api: NetworkTodoListApi,
    private val todoDao: TodoDao
) : TodoRepository {


    override fun observeTodoItems(): Flow<List<TodoItem>> {
        return todoDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshTodoItems(listName: String): Result<Unit> {
        return try {
            val response = api.getTodoItems(GetItemsBody(listName))
            val entities = response.serviceResponseDto.myTasksDto.items.map { it.toEntity() }
            todoDao.insertItems(entities)

            Result.success(Unit)
        }catch (e : Exception){
            Result.failure(e)
        }
    }

    override suspend fun getTodoItems(listName: String): NetworkResult<List<TodoItem>> {

        return safeApiCall {
            val response = api.getTodoItems(GetItemsBody(listName))

            response.serviceResponseDto.myTasksDto.items.map {
                it.toDomain()
            }
        }

    }


    override suspend fun addTodoItem(
        entityName: String,
        listName: String
    ): NetworkResult<Response> {
        return safeApiCall {
            api.addTodoItem(AddItemBody(listName = listName, itemName = entityName)).toDomain()
        }
    }


    override suspend fun deleteTodoItem(entityId: String): NetworkResult<Response> {
        return safeApiCall {
            val response = api.deleteTodoItem(
                DeleteTodoItemRequestBody(
                    itemId = entityId,
                    listId = "todo.moi_dela"
                )
            ).toDomain()

            response
        }
    }
}