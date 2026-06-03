package com.powakaz.feature_shopping.presentation.list

import android.widget.Toast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.R
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.usecase.DeleteShoppingItemUseCase
import com.powakaz.feature_shopping.domain.usecase.GetShoppingItemsUseCase
import com.powakaz.feature_shopping.domain.usecase.UpdateShoppingItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val getShoppingItemsUseCase: GetShoppingItemsUseCase,
    private val updateShoppingItemsUseCase: UpdateShoppingItemUseCase,
    private val deleteShoppingItemsUseCase: DeleteShoppingItemUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    init {
        loadItems()

    }


    fun loadItems() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            when (val result = getShoppingItemsUseCase()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            items = result.data,
                            isLoading = false
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                is NetworkResult.Exception -> {
                    _uiState.update { it.copy(isLoading = false, error = result.e.message) }
                }

            }
        }
    }

    fun toggleItem(itemId: String) {
        val item = _uiState.value.items.find { it.id == itemId } ?: return
        val newStatus = !item.isCompleted

        updateLocalStatus(itemId, newStatus)

        viewModelScope.launch {
            val result = updateShoppingItemsUseCase(itemId, newStatus)

            if (result !is NetworkResult.Success) {
                updateLocalStatus(itemId, !newStatus)

                //сообщение об ошибке
                _uiState.update {
                    it.copy(errorResId = R.string.error_update_item) //text message
                }
                //убираем сообщение
                viewModelScope.launch {
                    delay(6000)
                    _uiState.update { it.copy(errorResId = null) }
                }
            }
        }
    }

    private fun updateLocalStatus(itemId: String, isCompleted: Boolean) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.map { it ->
                if (it.id == itemId) {
                    it.copy(isCompleted = !it.isCompleted)
                } else {
                    it
                }
            }
            currentState.copy(items = updatedItems)
        }
    }

    fun deleteItem(itemId: String){
        val oldShoppingList = _uiState.value.items

        _uiState.update { state ->
            state.copy(items = state.items.filter { it.id != itemId })
        }

        viewModelScope.launch {
            val result = deleteShoppingItemsUseCase(itemId)

            if (result !is NetworkResult.Success) {
                _uiState.update {
                    it.copy(items = oldShoppingList, errorResId = R.string.error_update_item)
                }
            }
        }
    }

    data class ShoppingListUiState(
        val items: List<ShoppingItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val errorResId: Int? = null
    ) {
        val boughtItems: List<ShoppingItem>
            get() = items.filter { it.isCompleted }

        val notBoughtItems: List<ShoppingItem>
            get() = items.filter { !it.isCompleted }
    }
}