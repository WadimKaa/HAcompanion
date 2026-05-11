package com.powakaz.feature_tasks.presentation.todo_list

import androidx.lifecycle.ViewModel
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) : ViewModel() {


}