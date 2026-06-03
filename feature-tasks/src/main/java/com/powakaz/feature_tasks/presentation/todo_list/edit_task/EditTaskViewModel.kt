package com.powakaz.feature_tasks.presentation.todo_list.edit_task

import androidx.lifecycle.ViewModel
import com.powakaz.feature_tasks.domain.usecase.ChangeStateTodoItemUseCase
import javax.inject.Inject


data class EditTaskUIState(
    val taskName: String = "",
    val isTextFieldFocused: Boolean = false,
    val lengthTextLimit: Int = 100,
    val minLengthText: Int = 5,
    val wasFocusedOnce: Boolean = false,
    val isLoading : Boolean = false
) {
    val isNeedHideHead: Boolean = wasFocusedOnce
    val isOutLengthError: Boolean = taskName.length > lengthTextLimit
    val textCounter: String = "${taskName.length}/$lengthTextLimit"
    val canSave : Boolean = taskName.length >= minLengthText && !isOutLengthError
}

class EditTaskViewModel @Inject constructor(
    changeStateTodoItemUseCase: ChangeStateTodoItemUseCase
) :
    ViewModel() {


}