package com.powakaz.feature_tasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.powakaz.feature_tasks.data.local.SyncStatus
import com.powakaz.feature_tasks.data.local.TodoItemEntity
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


    @Transaction
    suspend fun clearAndInsert(items: List<TodoItemEntity>) {
        clearAll()
        insertItems(items)
    }

    @Transaction
    suspend fun syncItems(items: List<TodoItemEntity>) {
        insertItems(items)
        val ids = items.map { it.id }
        deleteExcept(ids)
    }

    @Query("DELETE FROM todo_items")
    suspend fun clearAll()

    @Query("UPDATE todo_items SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateStatus(id: String, isCompleted: Boolean)

    @Query("UPDATE todo_items SET title = :title WHERE id = :id")
    suspend fun updateName(id: String, title: String)

    @Query("UPDATE todo_items SET serverId = :serverId WHERE title = :title")
    suspend fun updateRemoteId(serverId: String, title: String)


    @Query("DELETE FROM todo_items WHERE id NOT IN (:remainingIds)")
    suspend fun deleteExcept(remainingIds: List<String>)

}