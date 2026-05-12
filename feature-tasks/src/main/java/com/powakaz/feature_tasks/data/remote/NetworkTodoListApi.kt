package com.powakaz.feature_tasks.data.remote

import com.powakaz.feature_tasks.data.remote.model.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.TodoItemsResponseDto
import com.powakaz.feature_tasks.data.remote.model.GetItemsBody
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkTodoListApi {


    @POST("api/services/todo/get_items?return_response")
    suspend fun getTodoItems(
        @Body request: GetItemsBody
    ): TodoItemsResponseDto



    @POST("api/services/todo/add_item")
    suspend fun addTodoItem(@Body addItemBody: AddItemBody)
}