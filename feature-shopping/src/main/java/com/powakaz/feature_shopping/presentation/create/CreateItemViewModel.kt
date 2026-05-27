package com.powakaz.feature_shopping.presentation.create

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import com.powakaz.feature_shopping.presentation.input.InputStateTextField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.concurrent.atomics.update


@HiltViewModel
class CreateItemViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CreateItemUiState())
    val uiState: StateFlow<CreateItemUiState> = _uiState.asStateFlow()

    fun onTextChanged(newText: String) {
        _uiState.update { it.copy(text = newText, isTouched = true) }
    }


    fun onClearText() {
        _uiState.update { it.copy(text = "", isTouched = true) }
    }

    fun onSaveClick() {
        // Здесь в будущем будет вызов репозитория:
        // repository.saveItem(uiState.value.text)
        _uiState.update { it.copy(isSaved = true) }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}

data class CreateItemUiState(
    val text: String = "",
    val isTouched: Boolean = false,
    val isSaved: Boolean = false,
    val maxCharCount: Int = 30
) {

    val inputState: InputStateTextField
        get() = when {
            text.length > maxCharCount -> InputStateTextField.MoreCharactersLimit
            text.isNotBlank() -> InputStateTextField.NotEmptyInputField
            isTouched && text.isBlank() -> InputStateTextField.EmptyInputField
            else -> InputStateTextField.StartInputField
        }

    val canSave: Boolean get() = inputState is InputStateTextField.NotEmptyInputField
    val isError: Boolean get() = inputState is InputStateTextField.EmptyInputField
    val isLimitExceeded: Boolean get() = inputState is InputStateTextField.MoreCharactersLimit
    val showLabel: Boolean get() = inputState !is InputStateTextField.StartInputField
    val showCounter: Boolean get() = text.isNotEmpty() || isLimitExceeded
    val showStartHeader: Boolean get() = inputState is InputStateTextField.StartInputField
}