package com.powakaz.feature_shopping.presentation.input

sealed class InputStateTextField {
    object StartInputField : InputStateTextField()
    object EmptyInputField : InputStateTextField()
    object NotEmptyInputField : InputStateTextField()
}