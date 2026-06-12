package com.powakaz.feature_tasks.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.local.SyncStatus
import com.powakaz.feature_tasks.data.local.dao.TodoDao
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.add_item.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.change_status_item.ChangeStatusItemTodoBody
import com.powakaz.feature_tasks.data.remote.model.delete_item.DeleteTodoItemRequestBody
import com.powakaz.feature_tasks.data.remote.model.get_items.GetItemsBody
import com.powakaz.feature_tasks.data.remote.model.get_items.TodoItemsResponseDto
import com.powakaz.feature_tasks.data.remote.model.update_todo_item.UpdateTodoItemBody
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class TodoSyncWorker @AssistedInject constructor(
    @Assisted appContent: Context,
    @Assisted workerParams: WorkerParameters,
    private val networkApi: NetworkTodoListApi,
    private val todoDao: TodoDao
) : CoroutineWorker(appContent, workerParams) {

    override suspend fun doWork(): Result {
        val pendingItems = todoDao.getPendingSyncItems()

        if (pendingItems.isEmpty()) return Result.success()

        var shouldRetry = false

        pendingItems.forEach { itemEntity ->
            when (itemEntity.syncStatus) {
                SyncStatus.SYNCED -> {}
                SyncStatus.PENDING_INSERT -> {
                    val result = safeApiCall {
                        networkApi.addTodoItem(
                            AddItemBody(
                                listName = "todo.moi_dela",
                                itemName = itemEntity.title
                            )
                        )
                    }

                    when (result) {
                        is NetworkResult.Success<*> -> {
                            todoDao.updateSyncStatus(itemEntity.id, SyncStatus.PENDING_GET_ID)
                            shouldRetry = true
                        }

                        is NetworkResult.Error -> {
                            shouldRetry = true
                        }

                        is NetworkResult.Exception -> {
                            shouldRetry = true
                        }
                    }

                }

                SyncStatus.PENDING_GET_ID -> {
                    val result = safeApiCall {
                        networkApi.getTodoItems(GetItemsBody("todo.moi_dela"))
                    }

                    when (result) {
                        is NetworkResult.Success<TodoItemsResponseDto> -> {
                            val remoteItems = result.data.serviceResponseDto.myTasksDto.items
                            val choiceItem = remoteItems.find { it.title == itemEntity.title }

                            if (choiceItem != null) {
                                todoDao.updateRemoteId(choiceItem!!.id, choiceItem.title)
                                todoDao.markAsSynced(itemEntity.id)
                            } else {
                                shouldRetry = true
                            }
                        }

                        else -> {
                            shouldRetry = true
                        }
                    }
                }

                SyncStatus.PENDING_CHANGE_NAME -> {
                    val response = safeApiCall {
                        networkApi.renameTodoItem(
                            UpdateTodoItemBody(
                                listId = "todo.moi_dela",
                                entityId = itemEntity.serverId!!,
                                entityName = itemEntity.title
                            )
                        )
                    }


                    when (response) {
                        is NetworkResult.Success -> {
                            todoDao.markAsSynced(itemEntity.id)
                        }

                        else -> {
                            shouldRetry = true
                        }
                    }
                }

                SyncStatus.PENDING_CHANGE_STATUS -> {
                    val status = if (itemEntity.isCompleted) "completed" else "needs_action"

                    val response = safeApiCall {
                        networkApi.changeStatusTodoItem(
                            ChangeStatusItemTodoBody(
                                "todo.moi_dela",
                                itemEntity.serverId!!,
                                status
                            )
                        )
                    }

                    when (response) {
                        is NetworkResult.Success -> {
                            todoDao.markAsSynced(itemEntity.id)
                        }

                        else -> {
                            shouldRetry = true
                        }
                    }
                }

                SyncStatus.PENDING_DELETE -> {
                    val response = safeApiCall {
                        networkApi.deleteTodoItem(DeleteTodoItemRequestBody("todo.moi_dela", itemEntity.serverId!!))
                    }

                    when(response){
                        is NetworkResult.Success -> {
                            todoDao.deleteById(itemEntity.id)
                        }
                        else -> {
                            shouldRetry = true
                        }
                    }
                }

            }

        }

        return if (shouldRetry) Result.retry() else Result.success()
    }
}