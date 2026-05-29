package com.powakaz.hacompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.powakaz.feature_shopping.presentation.create.CreateItemScreen
import com.powakaz.feature_shopping.presentation.list.ShoppingListScreen
import com.powakaz.hacompanion.ui.theme.HAcompanionTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HAcompanionTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "shopping_list") {
                    composable("shopping_list") {
                        ShoppingListScreen(
                            onNavigateToCreate = { navController.navigate("create_item") }
                        )

                    }

                    composable("create_item") {
                        CreateItemScreen(
                            onNavigateToList = {
                                navController.navigate("shopping_list") {
                                    popUpTo("create_item") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
