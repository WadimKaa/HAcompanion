package com.powakaz.feature_shopping.domain.usecase

import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import javax.inject.Inject

class DeleteShoppingItemUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: String) = repository.deleteShoppingItem(itemId)
}