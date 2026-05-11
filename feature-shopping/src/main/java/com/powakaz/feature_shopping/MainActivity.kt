package com.powakaz.feature_shopping

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.powakaz.core_network.factory.NetworkFactory
import com.powakaz.core_network.factory.RetrofitFactory
import com.powakaz.core_network.interceptor.AuthInterceptor
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.model.ShoppingItemsResponseDto
import com.powakaz.feature_shopping.data.model.TodoRequest
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var repository: ShoppingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val authInterceptor =
            AuthInterceptor("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjZDllNzA0NTFkYjU0MGJjOTJkMDViY2FjYjI2YmIwMyIsImlhdCI6MTc3NzkwMzQyNSwiZXhwIjoyMDkzMjYzNDI1fQ.tTdwBmmGl2JS5WFq_XHdaE7oJMwGxBDluSjZfgfLHqI")
        val okHttpClient = NetworkFactory.createOkHttpClient(authInterceptor)
        val retrofitClient = RetrofitFactory.createRetrofit(
            baseUrl = "http://192.168.0.11:8123/",
            okHttpClient = okHttpClient
        )


        lifecycleScope.launch {
            val keke = safeApiCall {
                RetrofitFactory.createApi<ShoppingApi>(retrofitClient)
                    .getShoppingList(TodoRequest("todo.shopping_list"))
            }

            Log.d("LOL", keke.toString())
        }


    }
}