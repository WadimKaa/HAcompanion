package com.powakaz.feature_tasks.presentation.todo_list.edit_task

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import com.powakaz.feature_tasks.domain.usecase.RenameTaskUseCase
import com.powakaz.navigation_api.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class EditTaskUIState(
    val taskName: String = "",
    val isTextFieldFocused: Boolean = false,
    val lengthTextLimit: Int = 100,
    val minLengthText: Int = 5,
    val wasFocusedOnce: Boolean = true,
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val taskId: String = ""
) {
    val isNeedHideHead: Boolean = wasFocusedOnce
    val isOutLengthError: Boolean = taskName.length > lengthTextLimit
    val textCounter: String = "${taskName.length}/$lengthTextLimit"
    val canSave: Boolean = taskName.length >= minLengthText && !isOutLengthError
}

sealed interface EditTaskUIEvent {
    data class ChangeName(val name: String) : EditTaskUIEvent
    data class FocusChange(val isFocused: Boolean) : EditTaskUIEvent

    object ClearTextField : EditTaskUIEvent
    object Save : EditTaskUIEvent
}

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val renameTaskUseCase: RenameTaskUseCase,
    val getItemUseCase: GetTodoItemsUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUIState())
    val uiState: StateFlow<EditTaskUIState> = _uiState.asStateFlow()
    private val taskId = savedStateHandle.toRoute<Screen.EditTaskScreen>().itemId

    init {
        loadTasksList()
    }

    private fun loadTasksList() {
        viewModelScope.launch {
            val item = getItemUseCase(taskId)

            _uiState.update {
                it.copy(
                    taskName = item.title,
                    isTextFieldFocused = true,
                    wasFocusedOnce = true,
                    isCompleted = item.isCompleted,
                    taskId = item.id
                )
            }
        }
    }

    fun onEvent(editTaskUIEvent: EditTaskUIEvent) {
        when (editTaskUIEvent) {
            is EditTaskUIEvent.ChangeName -> {
                _uiState.update {
                    it.copy(taskName = editTaskUIEvent.name)
                }
            }

            is EditTaskUIEvent.FocusChange -> {
                _uiState.update {
                    it.copy(
                        isTextFieldFocused = editTaskUIEvent.isFocused,
                        wasFocusedOnce = editTaskUIEvent.isFocused || it.wasFocusedOnce
                    )
                }
            }

            EditTaskUIEvent.ClearTextField -> {
                _uiState.update {
                    it.copy(taskName = "")
                }
            }

            EditTaskUIEvent.Save -> {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                viewModelScope.launch {
                    val todoItem = TodoItem(
                        id = uiState.value.taskId,
                        title = uiState.value.taskName,
                        isCompleted = uiState.value.isCompleted
                    )

                    Log.e("LOL", todoItem.toString())

                    val response = renameTaskUseCase(
                       todoItem
                    )

                    when (response) {
                        is NetworkResult.Error -> {
                            Log.e("LOL", "error ${response.message}")
                        }

                        is NetworkResult.Exception -> Log.e(
                            "LOL",
                            "Exception ${response.e.message}"
                        )

                        is NetworkResult.Success<*> -> Log.e("LOL", "Success")
                    }
                }
            }
        }
    }
}