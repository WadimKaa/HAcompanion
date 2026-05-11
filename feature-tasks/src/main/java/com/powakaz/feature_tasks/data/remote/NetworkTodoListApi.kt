package com.powakaz.feature_tasks.data.remote

import com.powakaz.feature_tasks.data.remote.model.TodoItemDto
import com.powakaz.feature_tasks.data.remote.model.TodoItemsResponseDto
import com.powakaz.feature_tasks.data.remote.model.TodoRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkTodoListApi {


    @POST("api/services/todo/get_items?return_response")
    suspend fun getTodoItems(
        @Body request: TodoRequest
    ): TodoItemsResponseDto
}