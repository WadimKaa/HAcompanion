package com.powakaz.hacompanion

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.powakaz.feature_tasks.presentation.todo_list.TodoListScreen
import com.powakaz.feature_tasks.presentation.todo_list.TodoListScreenAction
import com.powakaz.feature_tasks.presentation.todo_list.TodoListViewModel
import com.powakaz.feature_tasks.presentation.todo_list.add_task.TaskScreen
import com.powakaz.feature_tasks.presentation.todo_list.add_task.TaskScreenAction

@Composable
fun AppHavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TodoList
    ) {
        composable<Screen.TodoList> {
            val viewModel: TodoListViewModel = hiltViewModel()
            TodoListScreen(onAction = {
                when (it) {
                    is TodoListScreenAction.OnCreateTask -> {
                        navController.navigate(Screen.AddTodoItem)
                    }

                    is TodoListScreenAction.OnOpenTask -> {

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
    }
}