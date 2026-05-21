package com.powakaz.feature_shopping.presentation.create

import android.R.attr.enabled
import android.annotation.SuppressLint
import android.icu.text.CaseMap
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerDialogDefaults.Title
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.powakaz.feature_shopping.R
import com.powakaz.feature_shopping.presentation.input.InputState


@SuppressLint("ResourceAsColor")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CreateItemScreen() {

    var text by remember { mutableStateOf("") }
    var isTouched by remember { mutableStateOf(false) }


    val state = when {
        text.isNotBlank() -> InputState.NotEmptyInputField
        isTouched && text.isBlank() -> InputState.EmptyInputField
        else -> InputState.StartInputField
    }

    val isEnabledSaveButton = state is InputState.NotEmptyInputField

    val borderColor = when(state) {
        InputState.NotEmptyInputField ->  Color(R.color.btn_blue)
        InputState.EmptyInputField ->  Color.Red
        InputState.StartInputField ->  Color.Gray
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.CenterStart)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    clip = false
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorResource(R.color.create_arrow_light_grey))
                                .clickable { /* onClick */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ) {
            Spacer(modifier = Modifier.height(60.dp))

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

            Spacer(modifier = Modifier.height(60.dp))

            ///


            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    isTouched = true
                },

                singleLine = true,

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
                    .height(60.dp)
                    .padding(start = 24.dp, end = 24.dp),

                shape = RoundedCornerShape(12.dp),

                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = borderColor,
                    unfocusedIndicatorColor = borderColor,
                    cursorColor = Color.DarkGray
                )
            )


            Column(
                modifier = Modifier.weight(1f)
            ) {

            }
            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
                    .height(60.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(start = 24.dp, end = 24.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when(state) {
                                InputState.NotEmptyInputField -> Color(R.color.btn_blue)
                                InputState.EmptyInputField -> Color.Red
                                InputState.StartInputField -> Color.Gray
                            }
                        )
                        .clickable(enabled = isEnabledSaveButton) {
                            Toast
                                .makeText(context, "Сохранено", Toast.LENGTH_SHORT)
                                .show()
                        } ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
