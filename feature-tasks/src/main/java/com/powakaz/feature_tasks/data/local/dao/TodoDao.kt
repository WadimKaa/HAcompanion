package com.powakaz.feature_tasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.powakaz.feature_tasks.data.local.TodoItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items")
    fun observeAll(): Flow<List<TodoItemEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<TodoItemEntity>)

    @Update
    suspend fun updateItem(item : TodoItemEntity)


    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteById(id: String) : Int
}