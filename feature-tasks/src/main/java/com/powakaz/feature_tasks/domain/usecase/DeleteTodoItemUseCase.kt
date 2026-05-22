package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class DeleteTodoItemUseCase @Inject constructor(val todoRepository: TodoRepository) {
    suspend operator fun invoke(entityId: String) =
        todoRepository.deleteTodoItem(entityId = entityId)
}