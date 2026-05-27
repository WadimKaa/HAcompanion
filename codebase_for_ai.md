# Project HAcompanion Full Context

## Project Structure
- `:app`: Main entry point, UI theme, MainActivity, Navigation.
- `:core-network`: Retrofit factories, interceptors, generic network models, DI for networking.
- `:feature-tasks`: Task list feature (Clean Architecture).
- `:feature-shopping`: Shopping list feature (Clean Architecture).
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

### File: core-network/src/main/java/com/powakaz/core_network/di/NetworkModule.kt
```kotlin
package com.powakaz.core_network.di

import com.powakaz.core_network.BuildConfig
import com.powakaz.core_network.factory.NetworkFactory
import com.powakaz.core_network.factory.RetrofitFactory
import com.powakaz.core_network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(BuildConfig.HA_TOKEN)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return NetworkFactory.createOkHttpClient(authInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitFactory.createRetrofit(BuildConfig.HA_BASE_URL, okHttpClient)
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
import com.powakaz.feature_tasks.domain.model.TodoItem
import com.powakaz.feature_tasks.domain.usecase.GetTodoItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TodoListState(
    val unCompletedItems: List<TodoItem> = emptyList(),
    val completedItems: List<TodoItem> = emptyList(),
) {
    val unCompletedItemsSize: String = unCompletedItems.size.toString()
    val completedItemsSize: String = completedItems.size.toString()
}

sealed interface TodoListUIEvent

@HiltViewModel
class TodoListViewModel @Inject constructor(private val todoItemsUseCase: GetTodoItemsUseCase) :
    ViewModel() {

    val state: StateFlow<TodoListState> = flow {
        val result = todoItemsUseCase("todo.moi_dela")
        emit(result)
    }.map { result ->
        when (result) {
            is NetworkResult.Success -> {
                val (completed, uncompleted) = result.data.partition { it.isCompleted }
                TodoListState(completedItems = completed, unCompletedItems = uncompleted)
            }
            is NetworkResult.Error -> TodoListState()
            is NetworkResult.Exception -> TodoListState()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodoListState()
    )

    fun onEvent(todoListUIEvent: TodoListUIEvent) {}
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/domain/repository/TodoRepository.kt
```kotlin
package com.powakaz.feature_tasks.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_tasks.domain.model.Response
import com.powakaz.feature_tasks.domain.model.TodoItem

interface TodoRepository {
    suspend fun getTodoItems(listName: String): NetworkResult<List<TodoItem>>
    suspend fun addTodoItem(entityName: String, listName: String): NetworkResult<Response>
    suspend fun deleteTodoItem(entityId: String): NetworkResult<Response>
}
```

### File: feature-tasks/src/main/java/com/powakaz/feature_tasks/data/repository/TodoRepositoryImpl.kt
```kotlin
package com.powakaz.feature_tasks.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_tasks.data.mapper.toDomain
import com.powakaz.feature_tasks.data.remote.NetworkTodoListApi
import com.powakaz.feature_tasks.data.remote.model.add_item.AddItemBody
import com.powakaz.feature_tasks.data.remote.model.delete_item.DeleteTodoItemRequestBody
import com.powakaz.feature_tasks.data.remote.model.get_items.GetItemsBody
import com.powakaz.feature_tasks.domain.model.Response
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

    override suspend fun addTodoItem(entityName: String, listName: String): NetworkResult<Response> {
        return safeApiCall {
            api.addTodoItem(AddItemBody(listName = listName, itemName = entityName)).toDomain()
        }
    }

    override suspend fun deleteTodoItem(entityId: String): NetworkResult<Response> {
        return safeApiCall {
            api.deleteTodoItem(DeleteTodoItemRequestBody(itemId = entityId, listId = "todo.moi_dela")).toDomain()
        }
    }
}
```

---

## 🛒 Module: :feature-shopping

### File: feature-shopping/src/main/java/com/powakaz/feature_shopping/domain/model/ShoppingItem.kt
```kotlin
package com.powakaz.feature_shopping.domain.model

data class ShoppingItem(
    val id: String,
    val name: String,
    val isCompleted: Boolean
)
```

### File: feature-shopping/src/main/java/com/powakaz/feature_shopping/domain/repository/ShoppingRepository.kt
```kotlin
package com.powakaz.feature_shopping.domain.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.feature_shopping.domain.model.ShoppingItem

interface ShoppingRepository {
    suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>>
}
```

### File: feature-shopping/src/main/java/com/powakaz/feature_shopping/data/repository/ShoppingRepositoryImpl.kt
```kotlin
package com.powakaz.feature_shopping.data.repository

import com.powakaz.core_network.model.NetworkResult
import com.powakaz.core_network.utils.safeApiCall
import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.mapper.toDomain
import com.powakaz.feature_shopping.data.model.TodoRequest
import com.powakaz.feature_shopping.domain.model.ShoppingItem
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository

class ShoppingRepositoryImpl(private val api: ShoppingApi) : ShoppingRepository {
    override suspend fun getShoppingList(): NetworkResult<List<ShoppingItem>> {
        val result = safeApiCall {
            api.getShoppingList(TodoRequest(entityId = "todo.shopping_list"))
        }
        return when (result) {
            is NetworkResult.Success -> {
                val itemsDto = result.data.serviceResponse.todoShoppingList.items
                NetworkResult.Success(itemsDto.map { it.toDomain() })
            }
            is NetworkResult.Error -> NetworkResult.Error(result.code, result.message)
            is NetworkResult.Exception -> NetworkResult.Exception(result.e)
        }
    }
}
```

### File: feature-shopping/src/main/java/com/powakaz/feature_shopping/di/ShoppingModule.kt
```kotlin
package com.powakaz.feature_shopping.di

import com.powakaz.feature_shopping.data.api.ShoppingApi
import com.powakaz.feature_shopping.data.repository.ShoppingRepositoryImpl
import com.powakaz.feature_shopping.domain.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShoppingModule {
    @Provides
    @Singleton
    fun provideShoppingApi(retrofit: Retrofit): ShoppingApi {
        return retrofit.create(ShoppingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(api: ShoppingApi): ShoppingRepository {
        return ShoppingRepositoryImpl(api)
    }
}
```

---

## 📱 Module: :app

### File: app/src/main/java/com/powakaz/hacompanion/Screens.kt
```kotlin
package com.powakaz.hacompanion

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object TodoList : Screen
    @Serializable
    data object AddTodoItem : Screen
}
```

### File: app/src/main/java/com/powakaz/hacompanion/AppNavHost.kt
```kotlin
package com.powakaz.hacompanion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.powakaz.feature_tasks.presentation.todo_list.TodoListScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TodoList
    ) {
        composable<Screen.TodoList> {
            TodoListScreen()
        }
        composable<Screen.AddTodoItem> {
            // NewItemScreen()
        }
    }
}
```

### File: app/src/main/java/com/powakaz/hacompanion/MainActivity.kt
```kotlin
package com.powakaz.hacompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.powakaz.hacompanion.ui.theme.HAcompanionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HAcompanionTheme {
                AppNavHost()
            }
        }
    }
}
```
