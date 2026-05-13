# Project HAcompanion Full Context

## Project Structure
- `:app`: Main entry point, UI theme, MainActivity.
- `:core-network`: Retrofit factories, interceptors, generic network models.
- `:feature-tasks`: Task list feature (Clean Architecture: data, domain, presentation).
- `:feature-shopping`: Empty feature module.
- `:data`: Core data module (currently empty).

---

## 🛠 Build & Configuration

### File: settings.gradle.kts
```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HAcompanion"
include(":app")
include(":core-network")
include(":data")
include(":feature-tasks")
include(":feature-shopping")
```

### File: build.gradle.kts (Root)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
```

---

## 🌐 Module: :core-network

### File: core-network/src/main/java/com/powakaz/core_network/model/NetworkResult.kt
```kotlin
package com.powakaz.core_network.model

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val code: Int, val message: String?) : NetworkResult<Nothing>
    data class Exception(val e: Throwable) : NetworkResult<Nothing>
}
```

### File: core-network/src/main/java/com/powakaz/core_network/utils/SafeApiCall.kt
```kotlin
package com.powakaz.core_network.utils

import com.powakaz.core_network.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val result = apiCall()
            NetworkResult.Success(result)
        } catch (e: HttpException) {
            NetworkResult.Error(code = e.code(), message = e.message())
        } catch (e: IOException) {
            NetworkResult.Exception(e)
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }
}
```

### File: core-network/src/main/java/com/powakaz/core_network/interceptor/AuthInterceptor.kt
```kotlin
package com.powakaz.core_network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val token : String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(modifiedRequest)
    }
}
```

### File: core-network/src/main/java/com/powakaz/core_network/factory/RetrofitFactory.kt
```kotlin
package com.powakaz.core_network.factory

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitFactory {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun createRetrofit(baseUrl : String, okHttpClient: OkHttpClient) : Retrofit{
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    inline fun <reified T> createApi(retrofit: Retrofit): T {
        return retrofit.create(T::class.java)
    }
}
```

### File: core-network/src/main/java/com/powakaz/core_network/factory/NetworkFactory.kt
```kotlin
package com.powakaz.core_network.factory

import com.powakaz.core_network.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object NetworkFactory {
    fun createOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
```

---

## ✅ Module: :feature-tasks

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/presentation/todo_list/TodoListViewModel.kt
```kotlin
package com.powakaz.feature_tasks.presentation.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state = _state.asStateFlow()

    init {
        loadTodos("")
    }

    fun loadTodos(entityId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = todoItemsUseCase(entityId)) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, items = result.data) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = "Server error: ${result.code}") }
                }
                is NetworkResult.Exception -> {
                    _state.update { it.copy(isLoading = false, errorMessage = "Network exception: ${result.e.localizedMessage}") }
                }
            }
        }
    }
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/presentation/todo_list/TodoListState.kt
```kotlin
package com.powakaz.feature_tasks.presentation.todo_list

import com.powakaz.feature_tasks.domain.model.TodoItem

data class TodoListState(
    val isLoading : Boolean = false,
    val items : List<TodoItem> = emptyList(),
    val errorMessage : String? = null
)
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/domain/model/TodoItem.kt
```kotlin
package com.powakaz.feature_tasks.domain.model

data class TodoItem(
    val id : String,
    val title : String,
    val isCompleted : Boolean
)
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/domain/repository/TodoRepository.kt
```kotlin
package com.powakaz.feature_tasks.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.TodoItem

interface TodoRepository {
    suspend fun getTodoItems(listName: String): NetworkResult<List<TodoItem>>
    suspend fun addTodoItem(entityName: String, listName : String)
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/domain/usecase/GetTodoItemsUseCase.kt
```kotlin
package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoItemsUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(entityId : String) = repository.getTodoItems(entityId)
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/domain/usecase/AddTodoItemsUseCase.kt
```kotlin
package com.powakaz.feature_tasks.domain.usecase

import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoItemsUseCase @Inject constructor(private val repository: TodoRepository) {
    suspend operator fun invoke(itemName : String, listName : String) = repository.addTodoItem(itemName, listName)
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/data/repository/TodoRepositoryImpl.kt
```kotlin
package com.powakaz.feature_tasks.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.mapper.toDomain
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.GetItemsBody
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val api: NetworkTodoListApi) : TodoRepository {
    override suspend fun getTodoItems(listName: String): NetworkResult<List<TodoItem>> {
        return safeApiCall {
            val response = api.getTodoItems(GetItemsBody(listName))
            response.serviceResponseDto.myTasksDto.items.map { it.toDomain() }
        }
    }

    override suspend fun addTodoItem(entityName: String, listName: String) {
        safeApiCall { api.addTodoItem(AddItemBody(entityName, listName)) }
    }
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/data/mapper/TodoMapper.kt
```kotlin
package com.powakaz.feature_tasks.data.mapper

import com.powakaz.feature_tasks.data.remote.model.TodoItemDto
import com.powakaz.feature_tasks.domain.model.TodoItem

fun TodoItemDto.toDomain() : TodoItem{
    return TodoItem(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted == "completed"
    )
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/data/remote/NetworkTodoListApi.kt
```kotlin
package com.powakaz.feature_tasks.data.remote

import com.powakaz.feature_tasks.data.remote.model.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.TodoItemsResponseDto
import com.powakaz.feature_tasks.data.remote.model.GetItemsBody
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkTodoListApi {
    @POST("api/services/todo/get_items?return_response")
    suspend fun getTodoItems(@Body request: GetItemsBody): TodoItemsResponseDto

    @POST("api/services/todo/add_item")
    suspend fun addTodoItem(@Body addItemBody: AddItemBody)
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/data/remote/model/TodoItemsNetworkResponseDtos.kt
```kotlin
package com.powakaz.feature_tasks.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemsResponseDto(
    @SerialName("changed_states")
    val changedStates : List<String>,
    @SerialName("service_response")
    val serviceResponseDto : ServiceResponseDto
)

@Serializable
data class ServiceResponseDto(
    @SerialName("todo.moi_dela")
    val myTasksDto : MyTasksDto
)

@Serializable
data class MyTasksDto(
    @SerialName("items")
    val items : List<TodoItemDto>
)

@Serializable
data class TodoItemDto(
    @SerialName("uid")
    val id : String,
    @SerialName("summary")
    val title : String,
    @SerialName("status")
    val isCompleted : String
)
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/di/NetworkModule.kt
```kotlin
package com.powakaz.feature_tasks.di

import com.powakaz.core_network.factory.NetworkFactory
import com.powakaz.core_network.factory.RetrofitFactory
import com.powakaz.core_network.interceptor.AuthInterceptor
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.powakaz.feature_tasks.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideApi() : NetworkTodoListApi{
        val authInterceptor = AuthInterceptor(BuildConfig.AUTH_TOKEN)
        val okhttpClient = NetworkFactory.createOkHttpClient(authInterceptor)
        val retrofitClient = RetrofitFactory.createRetrofit(BuildConfig.BASE_URL, okhttpClient)
        return RetrofitFactory.createApi(retrofitClient)
    }
}
```

---

## 📱 Module: :app

### File: app/src/main/java/com/powakaz/hacompanion/HACompanionApp.kt
```kotlin
package com.powakaz.hacompanion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HACompanionApp : Application()
```

### File: app/src/main/java/com/powakaz/hacompanion/MainActivity.kt
```kotlin
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
import com.powakaz.hacompanion.ui.theme.HAcompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HAcompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(name = "Android", modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
```
