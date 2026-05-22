package com.powakaz.feature_shopping.presentation.create

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.powakaz.feature_shopping.R
import com.powakaz.feature_shopping.presentation.input.InputStateTextField


@SuppressLint("ResourceAsColor")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CreateItemScreen() {

    var text by remember { mutableStateOf("") }
    var isTouched by remember { mutableStateOf(false) }


    val stateText = when {
        text.isNotBlank() -> InputStateTextField.NotEmptyInputField
        isTouched && text.isBlank() -> InputStateTextField.EmptyInputField
        else -> InputStateTextField.StartInputField
    }

    val textEntered = stateText is InputStateTextField.NotEmptyInputField
    val textNotEntered = stateText is InputStateTextField.EmptyInputField
    val showStartHeader = stateText is InputStateTextField.StartInputField
    val showLabel =
        stateText is InputStateTextField.NotEmptyInputField || stateText is InputStateTextField.EmptyInputField



    val topSpacer by animateDpAsState(
        targetValue = if (showLabel) 0.dp else 60.dp
    )

    ///animation showHeader
    val showHeaderOffsetY by animateDpAsState(
        targetValue = if (showStartHeader) 0.dp else (-80).dp,
        animationSpec = tween(300)
    )

    val showHeaderAlpha by animateFloatAsState(
        targetValue = if (showStartHeader) 1f else 0f,
        animationSpec = tween(300)
    )

    val showHeaderScale by animateFloatAsState(
        targetValue = if (showStartHeader) 1f else 0.8f,
        animationSpec = tween(300)
    )

    ///// animation show Label "name text"
    val showLabelAlpha by animateFloatAsState(
        targetValue = if (showLabel) 1f else 0f,
        animationSpec = tween(300)
    )

    val showLabelOffSetY by animateDpAsState(
        targetValue = if (showLabel) 0.dp else 10.dp,
        animationSpec = tween(300)
    )

    ///// animation show Label "empty text"
    val showLabelEmptyTextAlpha by animateFloatAsState(
        targetValue = if (textNotEntered) 1f else 0f,
        animationSpec = tween(300)
    )

    val showLabelEmptyTextOffSetY by animateDpAsState(
        targetValue = if (textNotEntered) 0.dp else 10.dp,
        animationSpec = tween(300)
    )

    //// анимация подсчета введенного текста
    val showLabelCounterTextAlpha by animateFloatAsState(
        targetValue = if (textEntered) 1f else 0f,
        animationSpec = tween(300)
    )




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {

                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .size(40.dp)
                        ) {

                            Icon(
                                painter = painterResource(R.drawable.arrow),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.new_product),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            )

        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Spacer(modifier = Modifier.height(topSpacer))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    translationY = showHeaderOffsetY.toPx()
                    this.alpha = showHeaderAlpha
                    scaleX = showHeaderScale
                    scaleY = showHeaderScale
                }
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_shopping_item_add),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(160.dp)

                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(id = R.string.add_new_product),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = R.string.add_new_product_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )

            }

            Spacer(modifier = Modifier.height(topSpacer))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.product_name),
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 8.dp)
                        .graphicsLayer {
                            alpha = showLabelAlpha
                            translationY = showLabelOffSetY.toPx()
                        }
                )



                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        isTouched = true
                    },

                    trailingIcon = {
                        if (textEntered) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "Очистить",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(2.dp)
                                    .clickable {
                                        text = ""
                                    }
                            )
                        }
                    },

                    singleLine = true,

                    isError = textNotEntered,

                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black
                    ),

                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.product_name),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),

                    shape = RoundedCornerShape(12.dp),


                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red,
                        cursorColor = Color.DarkGray,
                        errorCursorColor = Color.DarkGray,
                        errorContainerColor = Color(0xFFFFFFFF),
                        focusedContainerColor = Color(0xFFFFFFFF),
                        unfocusedContainerColor = Color(0xFFFFFFFF)

                        //borderColorTextField
                    )
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_product),
                        fontSize = 12.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .graphicsLayer {
                                alpha = showLabelEmptyTextAlpha
                                translationY = showLabelEmptyTextOffSetY.toPx()
                            }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "12/105",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .graphicsLayer {
                                alpha = showLabelCounterTextAlpha
                            }

                    )
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            val context = LocalContext.current

            Button(
                onClick = {
                    Toast
                        .makeText(context, "Сохранено", Toast.LENGTH_SHORT)
                        .show()
                },
                enabled = textEntered,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

