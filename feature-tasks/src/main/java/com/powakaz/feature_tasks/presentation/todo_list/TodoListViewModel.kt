package com.powakaz.feature_tasks.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



data class TodoListState(
    val test : String = ""
)

sealed interface TodoListUIEvent{

}

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state = _state.asStateFlow()


    fun onEvent(todoListUIEvent: TodoListUIEvent){

    }


}