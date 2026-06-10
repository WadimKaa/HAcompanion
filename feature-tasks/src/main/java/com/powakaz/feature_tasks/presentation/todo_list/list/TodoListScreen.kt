package com.powakaz.feature_tasks.presentation.todo_list.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
            mainList = listOf(
                TodoItem("1", "hui", false),
                TodoItem("2", "pizda", false),
                TodoItem("3", "ne_hui", true),
                TodoItem("4", "i_ne_pizda", true)
            ),
            isCompletedListExpanded = true
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
                item(key = "uncompleted_head") {
                    UncompletedListHead(
                        inputState.unCompletedItemsSize,
                        isExpanded = inputState.isUnCompletedListExpanded,
                        onClickExpand = { onEvent(TodoListUIEvent.ChangeExpandUncompletedList) },
                        modifier = Modifier.animateItem()
                    )
                }
                if (inputState.isUnCompletedListExpanded) {
                    items(items = inputState.unCompletedItems, key = { it.id }) { item ->
                        SwipeToDeleteContainer(
                            onDelete = { onEvent(TodoListUIEvent.DeleteItem(item.id)) },
                            modifier = Modifier.animateItem()
                        ) {
                            UnCompletedTaskItem(
                                item = item,
                                onEvent = onEvent,
                                onAction = onAction
                            )
                        }
                    }
                }
                item(key = "completed_head") {
                    CompletedListHead(
                        size = inputState.completedItemsSize,
                        completedListExpanded = inputState.isCompletedListExpanded,
                        onExpandClick = {
                            onEvent(TodoListUIEvent.ChangeExpandCompletedList)
                        },
                        modifier = Modifier.animateItem()
                    )
                }
                if (inputState.isCompletedListExpanded)
                    items(items = inputState.completedItems, key = { it.id }) { item ->
                        SwipeToDeleteContainer(
                            onDelete = { onEvent(TodoListUIEvent.DeleteItem(item.id)) },
                            modifier = Modifier.animateItem()
                        ) {
                            CompletedTaskItem(
                                item = item,
                                modifier = Modifier.animateItem(),
                                onEvent = onEvent
                            )
                        }
                    }
            }

        }
    }

}


@Composable
fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Color(0xFFF44336) // Красный
            } else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        content = { content() } // Сама карточка задачи
    )
}

@Composable
fun CompletedTaskItem(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onEvent: (TodoListUIEvent) -> Unit
) {
    Column(modifier = modifier) {
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
                    .clickable(onClick = { onEvent(TodoListUIEvent.ChangeItemStatus(item.id)) })
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
}

@Composable
fun CompletedListHead(
    size: String,
    completedListExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (completedListExpanded) 180f else 0f
    )


    Column(modifier = modifier) {
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .clickable(onClick = { onExpandClick() })
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
                    .rotate(rotation)
            )
        }
    }
}

@Composable
fun UncompletedListHead(
    size: String,
    isExpanded: Boolean,
    onClickExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )


    Column(modifier = modifier) {
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .clickable(onClick = onClickExpand)
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
                    .rotate(rotation)
            )
        }
    }
}

@Composable
fun UnCompletedTaskItem(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onEvent: (TodoListUIEvent) -> Unit,
    onAction: (TodoListScreenAction) -> Unit
) {
    Column(modifier = modifier) {
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .background(Color(0xFFFFFFFF)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_circle_todos_list),
                contentDescription = null,
                tint = Color(0xFFd0cbf7),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(36.dp)
                    .clickable(onClick = { onEvent(TodoListUIEvent.ChangeItemStatus(item.id)) })
            )
            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF131826),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .clickable(onClick = { onAction(TodoListScreenAction.OnOpenTask(item.id)) })
            )
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9190a3),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(36.dp)
                    .clickable(onClick = { onAction(TodoListScreenAction.OnOpenTask(item.id)) })
            )
        }
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
