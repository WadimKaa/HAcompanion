package com.powakaz.feature_tasks.presentation.todo_list

import com.powakaz.feature_tasks.domain.model.TodoItem

data class TodoListState(
    val isLoading : Boolean,
    val items : List<TodoItem> = emptyList(),
    val errorMessage : String? = null
)
