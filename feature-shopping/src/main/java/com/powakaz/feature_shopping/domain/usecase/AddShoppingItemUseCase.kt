package com.powakaz.feature_shopping.domain.usecase

import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import javax.inject.Inject

class AddShoppingItemUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(name: String) = repository.addShoppingItem(name)
}