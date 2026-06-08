package com.powakaz.feature_tasks.data.remote

import com.powakaz.feature_tasks.data.remote.model.add_item.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.get_items.TodoItemsResponseDto
import com.powakaz.feature_tasks.data.remote.model.get_items.GetItemsBody
import com.powakaz.feature_tasks.data.remote.model.add_item.InfoEntityDto
import com.powakaz.feature_tasks.data.remote.model.change_status_item.ChangeStatusItemTodoBody
import com.powakaz.feature_tasks.data.remote.model.delete_item.DeleteTodoItemRequestBody
import com.powakaz.feature_tasks.data.remote.model.update_todo_item.UpdateTodoItemBody
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkTodoListApi {


    @POST("api/services/todo/get_items?return_response")
    suspend fun getTodoItems(
        @Body request: GetItemsBody
    ): TodoItemsResponseDto


    @POST("api/services/todo/add_item")
    suspend fun addTodoItem(
        @Body addItemBody: AddItemBody
    ): List<InfoEntityDto>


    @POST("api/services/todo/remove_item")
    suspend fun deleteTodoItem(
        @Body deleteItemRequestBody : DeleteTodoItemRequestBody
    ) : List<InfoEntityDto>


    @POST("api/services/todo/update_item")
    suspend fun changeStatusTodoItem(
        @Body changeStatusItemTodoBody: ChangeStatusItemTodoBody
    ) : List<InfoEntityDto>


    @POST("api/services/todo/update_item")
    suspend fun renameTodoItem(
        @Body updateTodoItemBody: UpdateTodoItemBody
    ) : List<InfoEntityDto>



}