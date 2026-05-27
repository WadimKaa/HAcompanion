package com.powakaz.feature_tasks.presentation.todo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.powakaz.feature_tasks.R
import com.powakaz.feature_tasks.domain.model.TodoItem


sealed interface TodoListScreenAction {
    data class OnOpenTask(val id: String) : TodoListScreenAction
    object OnCreateTask : TodoListScreenAction
}

@Composable
fun TodoListScreen(
    viewModel: TodoListViewModel = hiltViewModel(),
    onAction: (TodoListScreenAction) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TodoListContent(
        inputState = state,
        onEvent = viewModel::onEvent,
        onAction
    )

}


@Preview(showBackground = true)
@Composable
fun TodoListContentPreview(
) {
    TodoListContent(
        inputState = TodoListState(
            unCompletedItems = listOf(
                TodoItem("", "hui", false),
                TodoItem("", "pizda", false)
            ),
            completedItems = listOf(
                TodoItem("", "ne_hui", true),
                TodoItem("", "i_ne_pizda", true)
            )
        ),
        onEvent = {},
        onAction = {}
    )
}

@Composable
fun TodoListContent(
    inputState: TodoListState,
    onEvent: (TodoListUIEvent) -> Unit,
    onAction: (TodoListScreenAction) -> Unit
) {
    Scaffold(
        topBar = {
            TodoListTopBar(title = "Задачи", onAction)
        },
        containerColor = Color(0xFFfdfdfd)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                item {
                    UncompletedListHead(inputState.unCompletedItemsSize)
                }
                items(inputState.unCompletedItems.size) { index ->
                    UnCompletedTaskItem(inputState.unCompletedItems[index], index)
                }
                item {
                    CompletedListHead(inputState.completedItemsSize)
                }
                items(count = inputState.completedItems.size) { index ->
                    CompletedTaskItem(inputState.completedItems[index], index)
                }
            }

        }
    }

}

@Composable
fun CompletedTaskItem(item: TodoItem, index: Int) {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_tasks_completed_list_item),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(36.dp)
        )
        Text(
            text = item.title,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF131826),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF9190a3),
            modifier = Modifier
                .padding(end = 16.dp)
                .size(36.dp)
        )
    }
}

@Composable
fun CompletedListHead(size: String) {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_tasks_completed_task),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(42.dp)
                .padding(start = 4.dp),
            tint = Color(0xFF2EC14D)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
        ) {
            Text(
                text = "Завершенные",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF050810),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(text = size, fontSize = 16.sp, color = Color(0xFF9c9aa6))
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF8e8d9e),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
                .size(36.dp)
        )
    }
}

@Composable
fun UncompletedListHead(size: String) {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_circle_todos_list),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(42.dp)
                .padding(start = 4.dp),
            tint = Color(0xFF8690ff)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
        ) {
            Text(
                text = "Активные",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF050810),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(text = size, fontSize = 16.sp, color = Color(0xFF9c9aa6))
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF8e8d9e),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
                .size(36.dp)
        )
    }
}

@Composable
fun UnCompletedTaskItem(item: TodoItem, index: Int) {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_circle_todos_list),
            contentDescription = null,
            tint = Color(0xFFd0cbf7),
            modifier = Modifier
                .padding(start = 16.dp)
                .size(36.dp)
        )
        Text(
            text = item.title,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF131826),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF9190a3),
            modifier = Modifier
                .padding(end = 16.dp)
                .size(36.dp)
        )
    }
}


@Composable
fun TodoListTopBar(title: String, onAction: (TodoListScreenAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)

    ) {
        IconButton(
            onClick = { onAction(TodoListScreenAction.OnCreateTask) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(color = Color(0xFFe5e3fd), shape = CircleShape)
                .size(36.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                tint = Color(0xFF6651f7)
            )
        }

        Text(
            fontSize = 20.sp,
            text = title,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterStart)
        )


    }
}
