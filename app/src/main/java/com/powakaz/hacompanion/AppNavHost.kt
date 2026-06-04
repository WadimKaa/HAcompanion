package com.powakaz.hacompanion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.powakaz.feature_tasks.presentation.todo_list.list.TodoListScreen
import com.powakaz.feature_tasks.presentation.todo_list.list.TodoListScreenAction
import com.powakaz.feature_tasks.presentation.todo_list.add_task.TaskScreen
import com.powakaz.feature_tasks.presentation.todo_list.add_task.TaskScreenAction
import com.powakaz.feature_tasks.presentation.todo_list.edit_task.EditTaskScreen
import com.powakaz.feature_tasks.presentation.todo_list.edit_task.EditTaskScreenAction

@Composable
fun AppHavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TodoList
    ) {
        composable<Screen.TodoList> {
            TodoListScreen(onAction = {
                when (it) {
                    is TodoListScreenAction.OnCreateTask -> {
                        navController.navigate(Screen.AddTodoItem)
                    }

                    is TodoListScreenAction.OnOpenTask -> {
                        navController.navigate(Screen.EditTaskScreen)
                    }
                }
            })
        }
        composable<Screen.AddTodoItem> {
            TaskScreen(onAction = {
                when(it){
                    is TaskScreenAction.OnBack -> {
                        navController.popBackStack()
                    }
                }
            })
        }
        composable<Screen.EditTaskScreen>{
            EditTaskScreen(screenAction = {
                when(it){
                    is EditTaskScreenAction.OnBack -> {
                        navController.popBackStack()
                    }
                }
            })
        }
    }
}