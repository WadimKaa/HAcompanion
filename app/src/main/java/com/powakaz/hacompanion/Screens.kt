package com.powakaz.hacompanion

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    data object TodoList : Screen

    @Serializable
    data object AddTodoItem : Screen

    @Serializable
    data object EditTaskScreen : Screen
}