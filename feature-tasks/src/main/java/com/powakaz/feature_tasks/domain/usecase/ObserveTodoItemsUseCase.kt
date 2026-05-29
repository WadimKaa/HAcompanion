package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTodoItemsUseCase @Inject constructor(private val repository: TodoRepository) {
    operator fun invoke() : Flow<List<TodoItem>> = repository.observeTodoItems()
}