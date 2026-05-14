package com.powakaz.feature_shopping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val shoppingRepository: ShoppingRepository) :
    ViewModel() {


    //скрытое сост (для записи)
    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())

    //Публичное состояние (только для чтения)
    val items: StateFlow<List<ShoppingItem>> = _items.asStateFlow()

    init {
        // Загружаем данные сразу при создании
        loadShoppingList()
    }

    fun loadShoppingList() {
        viewModelScope.launch {
            val result = shoppingRepository.getShoppingList()
            if (result is NetworkResult.Success) {
                _items.value = result.data
            }
        }
    }
}