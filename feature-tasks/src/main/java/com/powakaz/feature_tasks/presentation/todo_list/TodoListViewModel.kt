package com.powakaz.feature_tasks.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TodoListState(
    val unCompletedItems: List<TodoItem> = emptyList(),
    val completedItems: List<TodoItem> = emptyList(),
    val isUnCompletedListExpanded: Boolean = false
) {
    val unCompletedItemsSize: String = unCompletedItems.size.toString()
    val completedItemsSize: String = completedItems.size.toString()
}

sealed interface TodoListUIEvent {

    object ChangeExpandUncompletedList : TodoListUIEvent

}

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            when (val result = todoItemsUseCase("todo.moi_dela")) {

                is NetworkResult.Success -> {

                    val (completed, uncompleted) =
                        result.data.partition { it.isCompleted }

                    _state.update {
                        it.copy(
                            completedItems = completed,
                            unCompletedItems = uncompleted
                        )
                    }
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Exception -> {

                }
            }
        }
    }


    fun onEvent(todoListUIEvent: TodoListUIEvent) {
        when (todoListUIEvent) {
            is TodoListUIEvent.ChangeExpandUncompletedList -> {
                _state.update {
                    it.copy(isUnCompletedListExpanded = !it.isUnCompletedListExpanded)
                }
            }
        }

    }


}