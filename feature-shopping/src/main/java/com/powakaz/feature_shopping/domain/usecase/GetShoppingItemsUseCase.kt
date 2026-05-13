package com.powakaz.feature_shopping.domain.usecase

import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetShoppingItemsUseCase @Inject constructor(private val shoppingRepository: ShoppingRepository) {
}