package com.powakaz.feature_shopping.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.powakaz.feature_shopping.R
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.presentation.list.components.ShoppingItemRow


@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    ShoppingListContent(
        ui_State = uiState,
        onNavigateToCreate = onNavigateToCreate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListContent(
    ui_State: ShoppingListUiState,
    onNavigateToCreate: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.padding(end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_basket),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(28.dp)

                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(id = R.string.shopping_list),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Cursive
                        )

                    }

                },

                actions = {
                    IconButton(
                        onClick = {
                            //удалить весь список
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .offset(x = (-10).dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_svg),
                            contentDescription = "Очистить",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(34.dp)
                        )

                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToCreate()
                },
                containerColor = Color(0xFF4CAF50),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 16.dp)
                    .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_add_24),
                    contentDescription = "Добавить товар",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }


    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {

            Divider(
                color = Color.LightGray,
                thickness = 1.dp
            )

            Text(
                modifier = Modifier.padding(16.dp, 10.dp),
                text = stringResource(id = R.string.need_to_buy),
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            )
            {
                items(ui_State.items) { item ->

                    ShoppingItemRow(
                        item = item,
                        onDeleteClick = {},
                        onCheckedChange = {}
                    )

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    ShoppingListContent(
        ui_State = ShoppingListUiState(
            items = listOf(
                ShoppingItem("1", "Молоко 1.5%", false),
                ShoppingItem("2", "Хлеб ржаной", true),
                ShoppingItem("3", "Молоко 1.5%", false),
                ShoppingItem("4", "Хлеб ржаной", true),

            )
        ),
        onNavigateToCreate = {}
    )
}
