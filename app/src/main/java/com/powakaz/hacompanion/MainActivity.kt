package com.powakaz.hacompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.powakaz.feature_shopping.presentation.create.CreateItemScreen
import com.powakaz.hacompanion.ui.theme.HAcompanionTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val shoppingViewModel: ShoppingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HAcompanionTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    //ShoppingScreen(viewModel = shoppingViewModel)
                    CreateItemScreen()
                }
            }
        }
    }
}


/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HAcompanionTheme {
        Greeting("Android")
    }
}*/