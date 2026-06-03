package com.powakaz.feature_shopping.presentation.list.components

import android.R.id
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.powakaz.feature_shopping.R
import com.powakaz.feature_shopping.domain.model.ShoppingItem


@Composable
fun ShoppingItemRow(
    key: String,
    item: ShoppingItem,
    onDeleteClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onCheckedChange(!item.isCompleted) }) {
                Icon(
                    painter = painterResource(
                        id = if (item.isCompleted) R.drawable.checker else R.drawable.checker_empty
                    ),
                    modifier = Modifier.size(26.dp),
                    contentDescription = null,
                    tint = if (item.isCompleted) Color(0xFF4CAF50) else Color.Gray
                )
            }

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_svg),
                    contentDescription = "Удалить",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Обычный товар")
@Composable
fun ShoppingItemRowPreview() {
    ShoppingItemRow(
        item = ShoppingItem(id = "1", name = "Молоко 1.5%", isCompleted = false),
        key = "",
        onDeleteClick = {},
        onCheckedChange = {}
    )
}

@Preview(showBackground = true, name = "Выполненный")
@Composable
fun ShoppingItemRowCompletedPreview() {
    ShoppingItemRow(
        item = ShoppingItem(id = "2", name = "Сыр", isCompleted = true),
        key = "",
        onDeleteClick = {},
        onCheckedChange = {}
    )
}