package com.powakaz.feature_shopping.presentation

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.powakaz.feature_shopping.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShoppingListScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier, //.padding(start = 10.dp), // отступ всей строки
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_basket),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                //.padding(start = 2.dp) // отступ свг
                                .size(22.dp)

                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(id = R.string.shopping_list),
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Cursive
                        )


                    }

                },

                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_svg),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(24.dp)
                            .clickable {
                                ///
                            }
                    )
                }
            )

        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {

            Divider(
                color = Color.LightGray,
                thickness = 1.dp
            )

            //Spacer(modifier = Modifier.padding(12.dp))

            Text(
                modifier = Modifier.padding(16.dp,10.dp),
                text = stringResource(id = R.string.need_to_buy),
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default
            )

            LazyColumn {
                // твой список
            }
        }
    }
}
