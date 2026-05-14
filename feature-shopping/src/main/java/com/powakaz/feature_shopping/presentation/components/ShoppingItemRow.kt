package com.powakaz.feature_shopping.presentation.components

import android.R.attr.text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.powakaz.feature_shopping.domain.model.ShoppingItem


@Composable
fun ShoppingItemRow(item: ShoppingItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { /* ... */ }
        )
        // Spacer и Text должны быть здесь, ВНУТРИ Row
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}