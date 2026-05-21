package com.powakaz.feature_shopping.presentation.input

sealed class InputState {
    object StartInputField : InputState()
    object EmptyInputField : InputState()
    object NotEmptyInputField : InputState()
}