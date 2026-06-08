package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class RenameTaskUseCase @Inject constructor(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todoItem: TodoItem) = todoRepository.renameTodoItem(todoItem)
}