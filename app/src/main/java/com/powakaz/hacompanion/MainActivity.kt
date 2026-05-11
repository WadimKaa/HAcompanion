package com.powakaz.hacompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.powakaz.core_network.factory.NetworkFactory
import com.powakaz.core_network.factory.RetrofitFactory
import com.powakaz.core_network.interceptor.AuthInterceptor
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.TodoRequest
import com.powakaz.hacompanion.ui.theme.HAcompanionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HAcompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}

@Composable
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
}