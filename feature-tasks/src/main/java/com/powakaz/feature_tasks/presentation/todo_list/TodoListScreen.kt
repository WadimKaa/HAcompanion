package com.powakaz.feature_tasks.presentation.todo_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TodoListScreen(
    // Получаем ViewModel через Hilt
    viewModel: TodoListViewModel = hiltViewModel()
) {
    // 1. Подписываемся на состояние (Данные ИЗ ViewModel в UI)
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 2. Отрисовка
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        state.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn {
            items(state.items) { item ->
                Text(text = item.title, modifier = Modifier.padding(16.dp))
            }
        }

        // Кнопка для передачи действия (Данные ИЗ UI во ViewModel)
        Button(
            onClick = { viewModel.loadTodos("some_id") },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("Reload")
        }
    }
}
