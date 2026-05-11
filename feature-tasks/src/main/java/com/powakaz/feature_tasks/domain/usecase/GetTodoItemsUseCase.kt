package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoItemsUseCase @Inject constructor(
    private val repository: TodoRepository
) {

    suspend operator fun invoke(entityId : String) = repository.getTodoItems(entityId)

}