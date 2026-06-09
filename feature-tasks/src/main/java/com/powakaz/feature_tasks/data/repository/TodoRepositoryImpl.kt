package com.powakaz.feature_tasks.data.repository

import android.util.Log
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.local.dao.TodoDao
import com.powakaz.feature_tasks.data.mapper.toDomain
import com.powakaz.feature_tasks.data.mapper.toEntity
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.add_item.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.change_status_item.ChangeStatusItemTodoBody
import com.powakaz.feature_tasks.data.remote.model.delete_item.DeleteTodoItemRequestBody
import com.powakaz.feature_tasks.data.remote.model.get_items.GetItemsBody
import com.powakaz.feature_tasks.data.remote.model.update_todo_item.UpdateTodoItemBody
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
            todoDao.syncItems(entities)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTodoItems(): NetworkResult<List<TodoItem>> {
        return safeApiCall {
            val response = api.getTodoItems(GetItemsBody("todo.moi_dela"))

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
            api.deleteTodoItem(
                DeleteTodoItemRequestBody(
                    itemId = entityId,
                    listId = "todo.moi_dela"
                )
            ).toDomain()

        }
    }

    override suspend fun changeStateTodoItem(
        entityId: String,
        isCompleted: Boolean
    ): NetworkResult<Response> {
        todoDao.updateStatus(entityId, isCompleted)

        val status = if (isCompleted) "completed" else "needs_action"
        return safeApiCall {
            api.changeStatusTodoItem(
                changeStatusItemTodoBody = ChangeStatusItemTodoBody(
                    listId = "todo.moi_dela",
                    itemId = entityId,
                    status = status
                )
            ).toDomain()
        }
    }


    override suspend fun renameTodoItem(todoItem: TodoItem): NetworkResult<Response> {
        todoDao.updateName(todoItem.id, todoItem.title)


        return safeApiCall {
            api.renameTodoItem(
                UpdateTodoItemBody(
                    listId = "todo.moi_dela",
                    entityId = todoItem.id,
                    entityName = todoItem.title
                )
            ).toDomain()
        }
    }

    override fun observeTodoItem(id: String): Flow<TodoItem?> {
        return todoDao.observeById(id).map {
            it?.toDomain()
        }
    }
}