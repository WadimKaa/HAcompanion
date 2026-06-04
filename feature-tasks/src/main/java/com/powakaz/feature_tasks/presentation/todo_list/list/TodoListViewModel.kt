package com.powakaz.feature_tasks.presentation.todo_list.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.usecase.ObserveTodoItemsUseCase
import com.powakaz.feature_tasks.domain.usecase.RefreshTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TodoListState(
    val mainList: List<TodoItem> = emptyList(),
    val isUnCompletedListExpanded: Boolean = false,
    val isCompletedListExpanded: Boolean = false,
) {

    val unCompletedItems: List<TodoItem> = mainList.filter { !it.isCompleted }
    val completedItems: List<TodoItem> = mainList.filter { it.isCompleted }

    val unCompletedItemsSize: String = unCompletedItems.size.toString()
    val completedItemsSize: String = completedItems.size.toString()
}

sealed interface TodoListUIEvent {
    data class ChangeItemStatus(val id: String) : TodoListUIEvent

    object ChangeExpandUncompletedList : TodoListUIEvent
    object ChangeExpandCompletedList : TodoListUIEvent

}

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val observeTodoItemsUseCase: ObserveTodoItemsUseCase,
    private val refreshTodoItemsUseCase: RefreshTodoItemsUseCase
) :
    ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state = _state.asStateFlow()

    private fun refreshData() {
        viewModelScope.launch {
            // Просто пинаем сеть. Результат прилетит в collect выше сам через базу.
            refreshTodoItemsUseCase("todo.moi_dela")
        }
    }

    init {
        viewModelScope.launch {
            observeTodoItemsUseCase().collect { items ->

                _state.update {
                    it.copy(
                        mainList = items
                    )
                }

            }
        }

        refreshData()
    }


    fun onEvent(todoListUIEvent: TodoListUIEvent) {
        when (todoListUIEvent) {
            is TodoListUIEvent.ChangeExpandUncompletedList -> {
                _state.update {
                    it.copy(isUnCompletedListExpanded = !it.isUnCompletedListExpanded)
                }
            }

            TodoListUIEvent.ChangeExpandCompletedList -> {
                _state.update {
                    it.copy(isCompletedListExpanded = !it.isCompletedListExpanded)
                }
            }

            is TodoListUIEvent.ChangeItemStatus -> {
                _state.update {
                    it.copy()
                }
            }
        }

    }


}