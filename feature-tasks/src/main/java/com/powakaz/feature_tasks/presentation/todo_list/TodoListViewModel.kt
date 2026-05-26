package com.powakaz.feature_tasks.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


data class TodoListState(
    val unCompletedItems: List<TodoItem> = emptyList(),
    val completedItems: List<TodoItem> = emptyList(),
) {
    val unCompletedItemsSize: String = unCompletedItems.size.toString()
    val completedItemsSize: String = completedItems.size.toString()
}

sealed interface TodoListUIEvent {

}

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) :
    ViewModel() {

    val state: StateFlow<TodoListState> = flow {
        val result = todoItemsUseCase("todo.moi_dela")
        emit(result)
    }.map { result ->
        when (result) {
            is NetworkResult.Success -> {
                val (completed, uncompleted) = result.data.partition { it.isCompleted }
                TodoListState(completedItems = completed, unCompletedItems = uncompleted)
            }

            is NetworkResult.Error -> {
                TodoListState()
            }

            is NetworkResult.Exception -> {
                TodoListState()
            }

        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodoListState() // С чего начинаем
    )


    fun onEvent(todoListUIEvent: TodoListUIEvent) {

    }


}