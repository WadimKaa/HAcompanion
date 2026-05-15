package com.powakaz.hacompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.powakaz.feature_tasks.presentation.todo_list.TodoListScreen
import com.powakaz.feature_tasks.presentation.todo_list.createNewTodoItem
import com.powakaz.hacompanion.ui.theme.HAcompanionTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HAcompanionTheme {
                createNewTodoItem()
            }
        }

    }
}





@Composable
fun DebugCounterScreen() {

    println("🔵 DebugCounterScreen ВЫЗВАН")

    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text("Счёт: $count", modifier = Modifier.fillMaxSize(), )

        Button(
            onClick = {
                count++
                println("🟢 Кнопка нажата, count = $count")
            }
        ) {
            Text("Увеличить")
        }
    }
}
