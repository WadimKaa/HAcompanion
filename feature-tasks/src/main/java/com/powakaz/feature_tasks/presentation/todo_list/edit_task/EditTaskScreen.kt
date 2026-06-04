package com.powakaz.feature_tasks.presentation.todo_list.edit_task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.powakaz.feature_tasks.R


sealed interface EditTaskScreenAction {
    object OnBack : EditTaskScreenAction
}

@Composable
fun EditTaskScreen(
    viewModel: EditTaskViewModel = hiltViewModel(),
    screenAction: (EditTaskScreenAction) -> Unit
) {
    val inputState by viewModel.uiState.collectAsStateWithLifecycle()
    EditTaskContent(inputState = inputState, onEvent = viewModel::onEvent, screenAction)
}

@Preview
@Composable
fun EditTaskPreview() {
    val inputState = EditTaskUIState(
        isTextFieldFocused = true,
        taskName = "Kek",
        wasFocusedOnce = true
    )

    EditTaskContent(inputState, {}, {})
}


@Composable
fun EditTaskContent(
    inputState: EditTaskUIState,
    onEvent: (EditTaskUIEvent) -> Unit,
    onAction: (EditTaskScreenAction) -> Unit
) {
    Scaffold(topBar = { TopBarCustom(onAction) }) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .imePadding()
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                AnimatedVisibility(visible = !inputState.isNeedHideHead) {
                    Head()
                }
                TextInput(inputState, onEvent)
                if (inputState.isOutLengthError) {
                    OutLengthError(inputState.lengthTextLimit)
                }
            }
            SaveButton(inputState, modifier = Modifier.align(Alignment.BottomCenter))
        }

    }
}

@Composable
fun SaveButton(inputState: EditTaskUIState, modifier: Modifier) {
    val disabledContainerColor =
        if (inputState.isOutLengthError) Color(0XFFfd7300) else Color(0xFFAF9CFF)
    val buttonText = if (inputState.isLoading) "Сохранение..." else "Сохранить изменения"

    Button(
        onClick = {},
        modifier = modifier
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0XFF6243f6),
            disabledContainerColor = disabledContainerColor
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = inputState.canSave
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

    }

}

@Composable
fun OutLengthError(lengthTextLimit: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .background(color = Color(0XFFffeee4), shape = RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_outlenght_warning),
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            tint = Color.Unspecified
        )
        Text(
            text = "Превышен лимит в $lengthTextLimit символов",
            modifier = Modifier.padding(start = 16.dp),
            color = Color(0XFFFF8C00)
        )
    }
}

@Composable
fun TextInput(inputState: EditTaskUIState, onEvent: (EditTaskUIEvent) -> Unit) {
    val focusedBorderColor =
        if (inputState.isOutLengthError) Color(0xFFf4c394) else Color(0XFFada3e4)
    val unFocusedBorderColor =
        if (inputState.isOutLengthError) Color(0xFFf4c394) else Color(0XFFe8e8e8)
    val hintTextColor = if (!inputState.isTextFieldFocused) Color(0XFFb5b4b9) else Color(0XFF8b8e97)
    val textCounterColor = if (inputState.isOutLengthError) Color(0XFFfd7302) else Color(0XFF929299)

    val textHintOffsetX by animateDpAsState(
        targetValue = if (inputState.isTextFieldFocused) 0.dp else 12.dp
    )
    val textHintOffsetY by animateDpAsState(
        targetValue = if (inputState.isTextFieldFocused) 0.dp else 42.dp
    )
    val textHintFontSize by animateFloatAsState(
        targetValue = if (inputState.isTextFieldFocused) 12f else 16f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        OutlinedTextField(
            value = inputState.taskName,
            onValueChange = {
                onEvent(EditTaskUIEvent.ChangeName(it))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unFocusedBorderColor
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
                .onFocusChanged{
                    onEvent(EditTaskUIEvent.FocusChange(it.isFocused))
                }

        )
        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .offset(x = textHintOffsetX, y = textHintOffsetY),
            text = "Название дела",
            color = hintTextColor,
            fontSize = textHintFontSize.sp
        )
        if (inputState.taskName.isNotEmpty()) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .align(
                        Alignment.CenterEnd
                    )
                    .padding(top = 4.dp, end = 24.dp)
                    .scale(0.8f)
                    .clickable(onClick = {
                        onEvent(EditTaskUIEvent.ClearTextField)
                    }),
                tint = Color(0XFFadacb1)
            )
        }
        if (inputState.wasFocusedOnce) {
            Text(
                text = inputState.textCounter,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp),
                fontSize = 12.sp,
                color = textCounterColor
            )
        }
    }
}

@Composable
fun Head() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.img_edit_task_head), contentDescription = null)
        Text(
            text = "Редактируйте дело",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Измените название дела, чтобы обновить его в списке",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0XFFacaeb2),
            modifier = Modifier
                .widthIn(max = 250.dp)
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun TopBarCustom(onAction: (EditTaskScreenAction) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
        IconButton(
            onClick = {
                onAction(EditTaskScreenAction.OnBack)
            },
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