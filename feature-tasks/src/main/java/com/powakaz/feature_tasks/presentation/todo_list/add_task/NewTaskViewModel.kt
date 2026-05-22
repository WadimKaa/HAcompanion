package com.powakaz.feature_tasks.presentation.todo_list.add_task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.usecase.AddTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TaskUiState(
    val taskName: String = "",
    val wasFocusedOnce: Boolean = false,
    val isFocused: Boolean = false,
    val maxLetterCount: Int = 10,
    val isLoading: Boolean = false,
    val isNeedShowNetErrorToast: Boolean = false,
    val isNeedShowExceptionToast: Boolean = false,
    val toastDelay: Long = 3_000L,
    val isSuccessSaved : Boolean = false
) {

    var isHeadVisible = !wasFocusedOnce
    var isTextCounterVisible = wasFocusedOnce
    val isLabelUp = isFocused || taskName.isNotEmpty()
    val isError: Boolean = taskName.length > maxLetterCount
    val canSave: Boolean = taskName.length in 3..maxLetterCount && !isError
}

sealed interface TaskUiEvent {
    data class TaskNameChanged(val name: String) : TaskUiEvent
    data class FocusChanged(val isFocused: Boolean) : TaskUiEvent
    object ClearTaskName : TaskUiEvent
    object SaveClicked : TaskUiEvent
    object ExceptionToastClose : TaskUiEvent
    object ErrorToastClose : TaskUiEvent
    object ClickOkButton : TaskUiEvent
    object ClickBackButton : TaskUiEvent
}


@HiltViewModel
class NewTaskViewModel @Inject constructor(private val addTodoItemsUseCase: AddTodoItemsUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()


    fun onEvent(event: TaskUiEvent) {
        when (event) {
            is TaskUiEvent.TaskNameChanged -> {
                _uiState.update {
                    it.copy(
                        taskName = event.name
                    )
                }
            }

            is TaskUiEvent.FocusChanged -> {
                _uiState.update {
                    it.copy(
                        isFocused = event.isFocused,
                        wasFocusedOnce = it.wasFocusedOnce || event.isFocused
                    )
                }
            }

            TaskUiEvent.ClearTaskName -> {
                _uiState.update { it.copy(taskName = "") }
            }

            TaskUiEvent.SaveClicked -> {
                saveTask()
            }

            TaskUiEvent.ErrorToastClose -> {
                _uiState.update {
                    it.copy(isNeedShowNetErrorToast = false)
                }
            }


            TaskUiEvent.ExceptionToastClose -> {
                _uiState.update {
                    it.copy(isNeedShowExceptionToast = false)
                }
            }


            TaskUiEvent.ClickOkButton -> {

            }


            TaskUiEvent.ClickBackButton -> {

            }
        }
    }


    private fun saveTask() {
        val currentState = _uiState.value
        if (currentState.canSave) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                val response = addTodoItemsUseCase(
                    itemName = currentState.taskName,
                    listName = "todo.moi_dela"
                )

                when (response) {
                    is NetworkResult.Success -> {
                        _uiState.update {
                            it.copy(isSuccessSaved = true)
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isNeedShowNetErrorToast = true,
                                isLoading = false
                            )
                        }

                        viewModelScope.launch {
                            delay(currentState.toastDelay)

                            _uiState.update {
                                it.copy(isNeedShowNetErrorToast = false)
                            }
                        }
                    }

                    is NetworkResult.Exception -> {
                        _uiState.update {
                            it.copy(
                                isNeedShowExceptionToast = true,
                                isLoading = false
                            )
                        }

                        viewModelScope.launch {
                            delay(currentState.toastDelay)

                            _uiState.update {
                                it.copy(isNeedShowExceptionToast = false)
                            }
                        }
                    }
                }

            }
        }
    }
}