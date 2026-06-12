package com.powakaz.feature_tasks.data.repository

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.local.SyncStatus
import com.powakaz.feature_tasks.data.local.TodoItemEntity
import com.powakaz.feature_tasks.data.local.dao.TodoDao
import com.powakaz.feature_tasks.data.mapper.toDomain
import com.powakaz.feature_tasks.data.mapper.toEntity
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.add_item.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.change_status_item.ChangeStatusItemTodoBody
import com.powakaz.feature_tasks.data.remote.model.delete_item.DeleteTodoItemRequestBody
import com.powakaz.feature_tasks.data.remote.model.get_items.GetItemsBody
import com.powakaz.feature_tasks.data.remote.model.update_todo_item.UpdateTodoItemBody
import com.powakaz.feature_tasks.data.worker.TodoSyncWorker
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val api: NetworkTodoListApi,
    private val todoDao: TodoDao,
    @ApplicationContext private val context: Context
) : TodoRepository {


    private fun triggerSync() {
        val workRequest = OneTimeWorkRequestBuilder<TodoSyncWorker>()
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()


        WorkManager.getInstance(context)
            .enqueueUniqueWork("todo_sync", ExistingWorkPolicy.REPLACE, workRequest)
    }


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

    override suspend fun getTodoItem(id : String): TodoItem {
        return todoDao.getTodoItem(id).toDomain()
    }


    override suspend fun addTodoItem(
        entityName: String,
        listName: String
    ): NetworkResult<Response> {
        todoDao.insertItem(
            TodoItemEntity(
                id = UUID.randomUUID().toString(),
                title = entityName,
                isCompleted = false,
                syncStatus = SyncStatus.PENDING_INSERT,
            )
        )

        triggerSync()
        return NetworkResult.Success(Response(isSuccess = true))
    }


    override suspend fun deleteTodoItem(entityId: String): NetworkResult<Response> {
        todoDao.markAsDeleted(entityId)
        triggerSync()
        return NetworkResult.Success(data = Response(isSuccess = true))
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