package com.powakaz.feature_tasks.presentation.todo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.powakaz.feature_tasks.R

@Composable
fun TodoListScreen(
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TodoListContent(
        inputState = state,
        onEvent = viewModel::onEvent
    )

}


@Preview(showBackground = true)
@Composable
fun TodoListContentPreview(
) {
    TodoListContent(
        inputState = TodoListState(),
        onEvent = {}
    )

}

@Composable
fun TodoListContent(inputState: TodoListState, onEvent: (TodoListUIEvent) -> Unit) {
    Scaffold(
        topBar = {
            TodoListTopBar(stringResource(R.string.create_task))
        },
        containerColor = Color(0xFFfdfdfd)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        }
    }

}


@Composable
fun TodoListTopBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)

    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterEnd).background(color = Color(0xFFe5e3fd), shape = CircleShape).size(36.dp)
        ) {
            Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = null, tint = Color(0xFF6651f7))
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
