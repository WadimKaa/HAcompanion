package com.powakaz.feature_shopping.presentation.list

import android.util.Log
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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


    fun loadItems(isPullToRefresh: Boolean = false) {
        viewModelScope.launch {

            if (isPullToRefresh) {
                _uiState.update { it.copy(isRefreshing = true) }
                delay(500)
            } else {
                if (_uiState.value.allList.isEmpty()) {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }

            when (val result = getShoppingItemsUseCase()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            allList = result.data,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    Log.e("LOL", result.data.size.toString())
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message
                        )
                    }
                }

                is NetworkResult.Exception -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.e.message
                        )
                    }
                }

            }
        }
    }

    fun toggleItem(itemId: String) {
        val item = _uiState.value.allList.find { it.id == itemId } ?: return
        val newStatus = !item.isCompleted

        updateLocalStatus(itemId, newStatus)

        viewModelScope.launch {
            val result = updateShoppingItemsUseCase(itemId, newStatus)

            if (result !is NetworkResult.Success) {
                updateLocalStatus(itemId, !newStatus)

                _uiState.update {
                    it.copy(errorResId = R.string.error_update_item) //text message
                }

                viewModelScope.launch {
                    delay(6000)
                    _uiState.update { it.copy(errorResId = null) }
                }
            }
        }
    }

    private fun updateLocalStatus(itemId: String, isCompleted: Boolean) {
        _uiState.update { currentState ->
            val updatedItems = currentState.allList.map { it ->
                if (it.id == itemId) {
                    it.copy(isCompleted = !it.isCompleted)
                } else {
                    it
                }
            }
            currentState.copy(allList = updatedItems)
        }
    }

    fun deleteItem(itemId: String) {
        val oldShoppingList = _uiState.value.allList

        _uiState.update { state ->
            state.copy(allList = state.allList.filter { it.id != itemId })
        }

        viewModelScope.launch {
            val result = deleteShoppingItemsUseCase(itemId)

            if (result !is NetworkResult.Success) {
                _uiState.update {
                    it.copy(allList = oldShoppingList, errorResId = R.string.error_update_item)
                }
            }
        }
    }

    fun toggleSelection(itemId: String) { ///
        _uiState.update { state ->
            val newSelection = state.selectedItems.toMutableSet()
            if (newSelection.contains(itemId)) {
                newSelection.remove(itemId)
            } else {
                newSelection.add(itemId)
            }
            state.copy(selectedItems = newSelection)
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedItems = emptySet()) }
    }

    fun deleteSelectedItems() {
        val selectedToDelete = _uiState.value.selectedItems.toList()

        if (selectedToDelete.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val allSuccess = parallelDeleteItem(selectedToDelete)

            clearSelection()
            loadItems()

            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    errorResId = if (!allSuccess) R.string.error_update_item else null
                )
            }
        }
    }

    private suspend fun parallelDeleteItem(selectedItemToDelete: List<String>): Boolean =
        coroutineScope {
            val result = selectedItemToDelete.map { itemId ->
                async {
                    deleteShoppingItemsUseCase(itemId)
                }
            }.awaitAll()

            result.all {
                it is NetworkResult.Success
            }
        }

    fun deleteAllItems() {
        val allItems = _uiState.value.allList

        if (allItems.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            val allSuccess = parallelDeleteAllItems(allItems)

            loadItems()

            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    errorResId = if (!allSuccess) R.string.error_update_item else null
                )
            }
        }
    }


    private suspend fun parallelDeleteAllItems(allItems: List<ShoppingItem>): Boolean = coroutineScope {

        val results = allItems.map { item ->
            async {
                deleteShoppingItemsUseCase(item.id)
            }
        }.awaitAll()

        results.all { it is NetworkResult.Success }
    }

    data class ShoppingListUiState(
        val allList: List<ShoppingItem> = emptyList(),
        val selectedItems: Set<String> = emptySet(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val errorResId: Int? = null,
        val isRefreshing: Boolean = false
    ) {
        val boughtItems: List<ShoppingItem>
            get() = allList.filter { it.isCompleted }

        val notBoughtItems: List<ShoppingItem>
            get() = allList.filter { !it.isCompleted }

    }
}