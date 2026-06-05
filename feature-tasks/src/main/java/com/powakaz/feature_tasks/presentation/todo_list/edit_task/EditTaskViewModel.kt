package com.powakaz.feature_tasks.presentation.todo_list.edit_task

import android.util.Log
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.powakaz.feature_tasks.domain.usecase.ChangeStateTodoItemUseCase
import com.powakaz.feature_tasks.presentation.todo_list.add_task.TaskUiState
import com.powakaz.navigation_api.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class EditTaskUIState(
    val taskName: String = "",
    val isTextFieldFocused: Boolean = false,
    val lengthTextLimit: Int = 100,
    val minLengthText: Int = 5,
    val wasFocusedOnce: Boolean = false,
    val isLoading: Boolean = false
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
}

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    changeStateTodoItemUseCase: ChangeStateTodoItemUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUIState())
    val uiState: StateFlow<EditTaskUIState> = _uiState.asStateFlow()
    private val taskId = savedStateHandle.toRoute<Screen.EditTaskScreen>().itemId

    init {
        Log.e("LOL", taskId)
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
        }
    }
}