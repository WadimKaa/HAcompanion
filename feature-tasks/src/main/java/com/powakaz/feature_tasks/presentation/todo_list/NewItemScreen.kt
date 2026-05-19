package com.powakaz.feature_tasks.presentation.todo_list

import android.R.attr.onClick
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.powakaz.feature_tasks.R


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TaskCreateScreen() {

    val MAX_LETTER_COUNT = 100

    var taskName by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var wasFocusedOnce by remember { mutableStateOf(false) }

    val isLabelUp = isFocused || taskName.isNotEmpty()
    var isTextCounterVisible = wasFocusedOnce
    var isHeadVisible = !wasFocusedOnce
    var isError = taskName.length > MAX_LETTER_COUNT


    val labelOffsetY by animateDpAsState(
        targetValue = if (isLabelUp) 0.dp else 42.dp // 0 - над полем, 40 - внутри поля
    )
    val labelOffsetX by animateDpAsState(
        targetValue = if (isLabelUp) 24.dp else 36.dp // 0 - над полем, 40 - внутри поля
    )
    val labelFontSize by animateFloatAsState(
        targetValue = if (isLabelUp) 12f else 16f
    )


    Scaffold(
        topBar = {
            CreateTopBar(stringResource(R.string.create_task))
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .padding(bottom = 98.dp)
            ) {
                AnimatedVisibility(visible = isHeadVisible) {
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
                Box() {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = {
                            taskName = it
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (isError) {
                                Color(0xFFfbb97d)
                            } else {
                                Color(0xFF6a50f1)
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
                            .onFocusChanged {
                                isFocused = it.isFocused

                                if (it.isFocused && !wasFocusedOnce) {
                                    wasFocusedOnce = true
                                }
                            }
                    )
                    Text(
                        text = "Название дела",
                        fontSize = labelFontSize.sp,
                        modifier = Modifier.offset(x = labelOffsetX, y = labelOffsetY)
                    )
                    if (wasFocusedOnce) {
                        Image(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 24.dp)
                                .size(size = 18.dp)
                                .clickable {
                                    taskName = ""
                                }
                        )
                    }
                    if (isTextCounterVisible) {
                        Text(
                            text = "${taskName.length}/${MAX_LETTER_COUNT}",
                            color = if (isError) Color(0xFFfbb97d) else Color(0xFFb6b7b9),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp)
                        )
                    }
                }
                if (isError) {
                    ShowOutWarning()
                }

            }
            Button(
                onClick = {

                },
                enabled = taskName.length in 3..100,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xFFFFFFFF),
                    containerColor = Color(0xFF6a50f1),
                    disabledContainerColor = Color(0xFFEBE5FC),
                    disabledContentColor = Color(0xFFFFFFFF),
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Сохранить",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }
}


@Composable
fun ShowOutWarning() {
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
            contentDescription = "")
        Text(
            text = "Превышен лимит в 100 символов",
            color = Color(0xFFdc855a),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 8.dp)
        )
    }

}


@Composable
fun CreateTopBar(title: String) {
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
            Box() {
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