package com.powakaz.navigation_api

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    data object TodoList : Screen

    @Serializable
    data object AddTodoItem : Screen

    @Serializable
    data class EditTaskScreen(val itemId : String) : Screen
}