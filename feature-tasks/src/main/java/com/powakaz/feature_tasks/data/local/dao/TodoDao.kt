package com.powakaz.feature_tasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.powakaz.feature_tasks.data.local.SyncStatus
import com.powakaz.feature_tasks.data.local.TodoItemEntity
import com.powakaz.feature_tasks.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items WHERE syncStatus != 'PENDING_DELETE'")
    fun observeAll(): Flow<List<TodoItemEntity>>

    @Query("SELECT * FROM todo_items WHERE id =:id")
    fun observeById(id: String): Flow<TodoItemEntity?>

    @Query("SELECT * FROM todo_items WHERE syncStatus != 'SYNCED'")
    suspend fun getPendingSyncItems(): List<TodoItemEntity>

    @Query("UPDATE todo_items SET syncStatus = 'PENDING_DELETE' WHERE id = :id")
    suspend fun markAsDeleted(id: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<TodoItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoItemEntity)

    @Update
    suspend fun updateItem(item: TodoItemEntity)


    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("UPDATE todo_items SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: SyncStatus)


    @Query("UPDATE todo_items SET syncStatus = 'SYNCED' WHERE id = :localId")
    suspend fun markAsSynced(localId: String)

    @Query("SELECT * FROM todo_items WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_GET_ID')")
    suspend fun getItemsWaitingForId(): List<TodoItemEntity>

    @Query("SELECT * FROM todo_items WHERE syncStatus = 'PENDING_DELETE'")
    suspend fun getPendingDeletes(): List<TodoItemEntity>

    @Query("DELETE FROM todo_items WHERE syncStatus = 'SYNCED' AND id NOT IN (:remainingIds)")
    suspend fun deleteSyncedExcept(remainingIds: List<String>)

    @Transaction
    suspend fun clearAndInsert(items: List<TodoItemEntity>) {
        clearAll()
        insertItems(items)
    }

    @Transaction
    suspend fun syncItems(serverItems: List<TodoItemEntity>) {
        val waitingForID = getItemsWaitingForId()
        val pendingDeletes = getPendingDeletes()

        val deletesIds = pendingDeletes.map { it.id }.toSet()
        val deleteTitles = pendingDeletes.map { it.title }.toSet()

        serverItems.forEach { serverItem ->
            if (serverItem.id in deletesIds || serverItem.title in deleteTitles) {
                return@forEach
            }

            val match = waitingForID.find { it.title == serverItem.title }
            if (match != null) {
                deleteById(match.id)
            }


            insertItem(serverItem)
        }

        val serverIds = serverItems.map { it.id }
        deleteSyncedExcept(serverIds)
    }

    @Query("DELETE FROM todo_items")
    suspend fun clearAll()

    @Query("UPDATE todo_items SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateStatus(id: String, isCompleted: Boolean)

    @Query("UPDATE todo_items SET title = :title WHERE id = :id")
    suspend fun updateName(id: String, title: String)

    @Query("UPDATE todo_items SET id = :serverId WHERE title = :title")
    suspend fun updateId(serverId: String, title: String)


    @Query("DELETE FROM todo_items WHERE id NOT IN (:remainingIds)")
    suspend fun deleteExcept(remainingIds: List<String>)


    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getTodoItem(id: String): TodoItemEntity

}