package com.powakaz.feature_shopping.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.powakaz.feature_shopping.presentation.components.ShoppingItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel) {

    val shoppingItems by viewModel.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Список покупок") })
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            // 6. Рисуем список из полученных данных
            items(shoppingItems) { item ->
                ShoppingItemRow(item = item)
            }
        }
    }

}