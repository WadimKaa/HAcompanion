package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class RefreshTodoItemsUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(listName: String): Result<Unit> {
        return repository.refreshTodoItems(listName)
    }
}