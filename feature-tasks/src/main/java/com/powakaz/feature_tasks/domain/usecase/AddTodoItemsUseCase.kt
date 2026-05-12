package com.powakaz.feature_tasks.domain.usecase

import android.R
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoItemsUseCase @Inject constructor(private val repository: TodoRepository) {

    suspend operator fun invoke(itemName : String, listName : String) = repository.addTodoItem(itemName, listName)
}