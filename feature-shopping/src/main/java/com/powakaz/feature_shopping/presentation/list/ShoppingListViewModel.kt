package com.powakaz.feature_shopping.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.usecase.GetShoppingItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val getShoppingItemsUseCase: GetShoppingItemsUseCase
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
}

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)