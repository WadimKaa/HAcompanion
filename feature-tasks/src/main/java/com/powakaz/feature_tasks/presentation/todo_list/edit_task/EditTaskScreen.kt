package com.powakaz.feature_tasks.presentation.todo_list.edit_task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditTaskScreen() {

}

@Preview
@Composable
fun editTaskContent() {
    Scaffold(topBar = { TopBarCustom() }) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            Head()
        }

    }
}

@Composable
fun Head() {
    Column() { }
}

@Composable
fun TopBarCustom() {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFFFFFFFF)
            ),
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Text(
            text = "Редактирование дела",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}