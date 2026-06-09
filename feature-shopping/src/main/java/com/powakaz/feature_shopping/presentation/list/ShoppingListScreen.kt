package com.powakaz.feature_shopping.presentation.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.powakaz.feature_shopping.R
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.presentation.list.components.ShoppingItemRow
import kotlinx.coroutines.delay


@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()


    ShoppingListContent(
        allShoppingList = uiState.allList,
        boughtItems = uiState.boughtItems,
        notBoughtItems = uiState.notBoughtItems,
        onNavigateToCreate = onNavigateToCreate,
        onToggleItem = { itemId ->
            viewModel.toggleItem(itemId)
        },
        onDeleteItem = { itemId ->
            viewModel.deleteItem(itemId)
        },
        isRefreshing = uiState.isRefreshing,
        onRefresh = {
            viewModel.loadItems(isPullToRefresh = true)
        },
        onToggleSelection = { itemId ->
            viewModel.toggleSelection(itemId)
        },
        onClearSelection = {
            viewModel.clearSelection()
        },
        onDeleteSelected = {
            viewModel.deleteSelectedItems()
        },
        selectedItems = uiState.selectedItems,
        onDeleteAllList = {
            viewModel.deleteAllItems()
        }

    )

    LaunchedEffect(uiState.errorResId) {
        uiState.errorResId?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListContent(
    allShoppingList: List<ShoppingItem>,
    boughtItems: List<ShoppingItem>,
    notBoughtItems: List<ShoppingItem>,
    onNavigateToCreate: () -> Unit,

    onToggleItem: (String) -> Unit,
    onDeleteItem: (String) -> Unit,

    isRefreshing: Boolean,
    onRefresh: () -> Unit,

    selectedItems: Set<String>,
    onToggleSelection: (String) -> Unit,
    onClearSelection: () -> Unit,
    onDeleteSelected: () -> Unit,

    onDeleteAllList: () -> Unit

) {
    val pullToRefreshState = rememberPullToRefreshState()
    val isInSelectionMode = selectedItems.isNotEmpty()

    Scaffold(
        topBar = {
            if (isInSelectionMode) {
                SelectionTopBar(
                    count = selectedItems.size,
                    onClose = onClearSelection,
                    onDelete = onDeleteSelected
                )
            } else {
                DefaultTopBar(
                    onDeleteAll = onDeleteAllList
                )
            }
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

            HorizontalDivider(
                color = Color.LightGray,
                thickness = 1.dp
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                state = pullToRefreshState,
                indicator = {
                    MyCustomIndicator(state = pullToRefreshState, isRefreshing = isRefreshing)
                },
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 10.dp)
                )
                {

                    if (notBoughtItems.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(16.dp, 10.dp),
                                text = stringResource(id = R.string.need_to_buy),
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default
                            )
                        }
                    }

                    items(notBoughtItems, key = { it.id }) { item ->
                        val isSelected = selectedItems.contains(item.id)
                        SwipeToDeleteItemContainer(item = item, onDelete = onDeleteItem) {
                            ShoppingItemRow(
                                item = item,
                                onDeleteClick = {
                                    onDeleteItem(item.id)
                                },
                                onCheckedChange = {
                                    onToggleItem(item.id)
                                },
                                isSelected = isSelected,
                                onToggleSelection = {
                                    onToggleSelection(item.id)
                                },
                                onClick = {
                                    if (isInSelectionMode) onToggleSelection(item.id)
                                    else { /* открыть детали? */
                                    }
                                }

                            )
                        }

                    }

                    if (boughtItems.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(16.dp, 10.dp),
                                text = stringResource(id = R.string.buy),
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Default
                            )
                        }
                    }

                    items(boughtItems, key = { it.id }) { item ->
                        val isSelected = selectedItems.contains(item.id)
                        SwipeToDeleteItemContainer(item = item, onDelete = onDeleteItem) {
                            ShoppingItemRow(
                                item = item,
                                onDeleteClick = {
                                    onDeleteItem(item.id)
                                },
                                onCheckedChange = {
                                    onToggleItem(item.id)
                                },
                                isSelected = isSelected,
                                onToggleSelection = {
                                    onToggleSelection(item.id)
                                },
                                onClick = {
                                    if (isInSelectionMode) onToggleSelection(item.id)
                                    else { /* открыть детали? */
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeToDeleteItemContainer(
    item: ShoppingItem,
    onDelete: (String) -> Unit,
    content: @Composable () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { it == SwipeToDismissBoxValue.EndToStart }
    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            delay(200)
            onDelete(item.id)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = { DismissItemBackground() },
        content = { content() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopBar(count: Int, onClose: () -> Unit, onDelete: () -> Unit) {
    TopAppBar(
        title = {

            Row(
                //  modifier = Modifier.padding(end = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(40.dp)
                        .offset(x = (-6).dp)
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "выйти",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "Выбрано: $count",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        },
        actions = {
            TextButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(130.dp)
                    .offset(x = (14).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_svg),
                    contentDescription = "Очистить выделенное",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = stringResource(id = R.string.delete),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold

                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50))
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(onDeleteAll: () -> Unit) {
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
            TextButton(
                onClick = {
                    onDeleteAll()
                },
                modifier = Modifier
                    .size(130.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_svg),
                    contentDescription = "Очистить всё",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = stringResource(id = R.string.delete_all),
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold

                )
            }
        }
    )
}


@Composable
fun DismissItemBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFF5252)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            painter = painterResource(id = R.drawable.delete_svg),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun MyCustomIndicator(state: PullToRefreshState, isRefreshing: Boolean) {

    var canShowIcon by remember { mutableStateOf(true) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            canShowIcon = false
        }
    }

    LaunchedEffect(state.distanceFraction) {
        if (state.distanceFraction <= 0.01f && !isRefreshing) {
            canShowIcon = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationY = state.distanceFraction * 160f
            },
        contentAlignment = Alignment.TopCenter
    ) {

        if (isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.size(44.dp),
                color = Color(0xFF4CAF50),
                strokeWidth = 4.dp,
                trackColor = Color.Green
            )

        } else {
            if (canShowIcon && state.distanceFraction > 0.01f) {
                Icon(
                    painter = painterResource(id = R.drawable.refresh_list),
                    contentDescription = null,
                    modifier = Modifier
                        .size(54.dp)
                        .alpha(state.distanceFraction.coerceIn(0f, 1f)),
                    tint = Color.Unspecified
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    ShoppingListContent(
        allShoppingList = listOf(),
        boughtItems = listOf(

            ShoppingItem("1", "Молоко 1.5%", false),
            ShoppingItem("2", "Хлеб ржаной", false),
            ShoppingItem("3", "Молоко 1.5%", false),
            ShoppingItem("4", "Хлеб ржаной", false)

        ),
        notBoughtItems = listOf(
            ShoppingItem("1", "Молоко 1.5%", false),
            ShoppingItem("2", "Хлеб ржаной", true),
            ShoppingItem("3", "Молоко 1.5%", false),
            ShoppingItem("4", "Хлеб ржаной", true)
        ),
        onNavigateToCreate = {},
        onToggleItem = {},
        onDeleteItem = {},
        onRefresh = {},
        isRefreshing = false,
        onToggleSelection = {},
        onClearSelection = {},
        onDeleteSelected = {},
        selectedItems = setOf("1", "2", "3"),
        onDeleteAllList = {}
    )
}

