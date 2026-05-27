package com.powakaz.feature_tasks.presentation.todo_list.add_task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.powakaz.feature_tasks.R


sealed interface TaskScreenAction {
    object OnBack : TaskScreenAction
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: NewTaskViewModel = hiltViewModel(),
    onAction: (TaskScreenAction) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TaskContent(
        inputState = state,
        onEvent = viewModel::onEvent,
        onAction
    )
}

@Preview(showBackground = true)
@Composable
fun TaskContentPreview() {
    // Создаем фейковое состояние для отображения в превью
    val fakeState = TaskUiState(
        taskName = "Купить мо",
        isSuccessSaved = true
    )

    TaskContent(
        inputState = fakeState,
        onEvent = {},
        onAction = {}
    )
}


@Composable
fun TaskContent(
    inputState: TaskUiState,
    onEvent: (TaskUiEvent) -> Unit,
    onAction: (TaskScreenAction) -> Unit
) {
    var backgroundColor = if (inputState.isSuccessSaved) Color(0xFFf3fdf7) else Color(0xFFfdfdfd)

    Scaffold(
        topBar = {
            TopBar(stringResource(R.string.create_task), onClickBackButton = {
                onAction(TaskScreenAction.OnBack)
            })
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!inputState.isSuccessSaved) {
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .padding(bottom = 98.dp)
                ) {
                    AnimatedVisibility(visible = inputState.isHeadVisible) {
                        Head()
                    }
                    TextInput(
                        inputState = inputState,
                        onTaskNamedChanged = {
                            onEvent(TaskUiEvent.TaskNameChanged(it))
                        },
                        onFocusChanged = {
                            onEvent(TaskUiEvent.FocusChanged(it))
                        }
                    )
                    if (inputState.isError) {
                        ErrorMessage(inputState)
                    }
                }
                ButtonSave(inputState, onClickButtonSave = {
                    onEvent(TaskUiEvent.SaveClicked)
                })
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_succes_saved),
                        contentDescription = "",
                        modifier = Modifier
                            .size(320.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Дело сохранено!",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "\"${inputState.taskName}\" добавлено в ваш список дел",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }



                ButtonOk(onClickButtonOk = {
                    onEvent(TaskUiEvent.ClickOkButton)
                })
            }
            AnimatedVisibility(
                visible = inputState.isNeedShowNetErrorToast,
                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            ) {
                NetErrorToast(onCloseClick = { onEvent(TaskUiEvent.ErrorToastClose) })
                DisposableEffect(Unit) {
                    onDispose {
                        onEvent(TaskUiEvent.ErrorToastClose)
                    }
                }
            }


            AnimatedVisibility(
                visible = inputState.isNeedShowExceptionToast,
                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            ) {
                ExceptionToast(onCloseClick = { onEvent(TaskUiEvent.ExceptionToastClose) })
                DisposableEffect(Unit) {
                    onDispose {
                        onEvent(TaskUiEvent.ExceptionToastClose)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.ButtonOk(onClickButtonOk: () -> Unit) {
    Button(
        onClick = {
            onClickButtonOk()
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color(0xFFFFFFFF),
            containerColor = Color(0xFF3ac765),
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = "Готово",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BoxScope.ExceptionToast(onCloseClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
            .background(shape = RoundedCornerShape(16.dp), color = Color(0xFF1e283e))
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .align(Alignment.BottomCenter)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_outlenght_warning),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = "Не удалось сохранить",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Что-то пошло не так.\nПопробуйте еще раз",
                color = Color(0x99FFFFFF),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        IconButton(
            onClick = {
                onCloseClick()
            },
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = Color(0xFFdee4ea)
            )
        }
    }
}

@Composable
fun BoxScope.NetErrorToast(onCloseClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
            .background(shape = RoundedCornerShape(16.dp), color = Color(0xFF1e283e))
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .align(Alignment.BottomCenter)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_exception),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(text = "Ошибка сети", color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Проверьте подключение\nи попробуйте еще раз",
                color = Color(0x99FFFFFF),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        IconButton(
            onClick = {
                onCloseClick()
            },
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = Color(0xFFdee4ea)
            )
        }
    }
}

@Composable
fun BoxScope.ButtonSave(inputState: TaskUiState, onClickButtonSave: () -> Unit) {
    val disabledContainerColor = if (inputState.isLoading) Color(0xFF8f78fa) else Color(0xFFEBE5FC)
    val buttonText = if (inputState.isLoading) "Сохранить..." else "Сохранить"
    Button(
        onClick = {
            onClickButtonSave()
        },
        enabled = inputState.canSave && !inputState.isLoading,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color(0xFFFFFFFF),
            containerColor = Color(0xFF6a50f1),
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = Color(0xFFFFFFFF),
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (inputState.isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.2f),
                strokeWidth = 3.dp,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(20.dp)
            )
        }
        Text(
            text = buttonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TextInput(
    inputState: TaskUiState,
    onTaskNamedChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    val labelOffsetY by animateDpAsState(
        targetValue = if (inputState.isLabelUp) 0.dp else 42.dp // 0 - над полем, 40 - внутри поля
    )
    val labelOffsetX by animateDpAsState(
        targetValue = if (inputState.isLabelUp) 24.dp else 36.dp // 0 - над полем, 40 - внутри поля
    )
    val labelFontSize by animateFloatAsState(
        targetValue = if (inputState.isLabelUp) 12f else 16f
    )

    Box() {
        OutlinedTextField(
            value = inputState.taskName,
            onValueChange = {
                onTaskNamedChanged(it)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (inputState.isError) {
                    Color(0xFFfbb97d)
                } else {
                    Color(0xFF6a50f1)
                },
                unfocusedBorderColor = if (inputState.isError) {
                    Color(0xFFfbb97d)
                } else {
                    Color(0xFF79747e)
                }
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
                .onFocusChanged {
                    onFocusChanged(it.isFocused)
                }
        )
        Text(
            text = "Название дела",
            fontSize = labelFontSize.sp,
            modifier = Modifier.offset(x = labelOffsetX, y = labelOffsetY)
        )
        if (inputState.wasFocusedOnce) {
            Image(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
                    .size(size = 18.dp)
                    .clickable {
                        onTaskNamedChanged("")
                    }
            )
        }
        if (inputState.isTextCounterVisible) {
            Text(
                text = "${inputState.taskName.length}/${inputState.maxLetterCount}",
                color = if (inputState.isError) Color(0xFFfbb97d) else Color(0xFFb6b7b9),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun Head() {
    Column() {
        Image(
            painter = painterResource(R.drawable.create_task_main_draw),
            contentDescription = null,
            modifier = Modifier
                .size(height = 156.dp, width = 156.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.create_new_task_head),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Text(
            text = stringResource(R.string.create_task_text),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun ErrorMessage(inputState: TaskUiState) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .background(color = Color(0xFFffefe2), shape = RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outlenght_warning),
            contentDescription = ""
        )
        Text(
            text = "Превышен лимит в ${inputState.maxLetterCount} символов",
            color = Color(0xFFdc855a),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        )
    }

}


@Composable
fun TopBar(title: String, onClickBackButton: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)

    ) {
        Surface(
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.size(height = 36.dp, width = 36.dp)
        ) {
            Box(modifier = Modifier.clickable(onClick = {
                onClickBackButton()
            })) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }

        Text(
            fontSize = 20.sp,
            text = title,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.Center)
        )


    }
}