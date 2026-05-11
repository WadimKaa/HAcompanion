package com.powakaz.feature_tasks.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state = _state.asStateFlow()


    init {
        loadTodos("")
    }


    fun loadTodos(entityId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }


            when (val result = todoItemsUseCase(entityId)) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            items = result.data
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Server error: ${result.code}"
                        )
                    }
                }

                is NetworkResult.Exception -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Network exception: ${result.e.localizedMessage}"
                        )
                    }
                }
            }
        }

    }


}