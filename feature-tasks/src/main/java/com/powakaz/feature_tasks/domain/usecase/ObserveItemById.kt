package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class ObserveItemById @Inject constructor(private val todoRepository: TodoRepository) {
    operator fun invoke(id : String) = todoRepository.observeTodoItem(id)
}