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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideApi() : NetworkTodoListApi{
        val authInterceptor = AuthInterceptor("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjZDllNzA0NTFkYjU0MGJjOTJkMDViY2FjYjI2YmIwMyIsImlhdCI6MTc3NzkwMzQyNSwiZXhwIjoyMDkzMjYzNDI1fQ.tTdwBmmGl2JS5WFq_XHdaE7oJMwGxBDluSjZfgfLHqI")
        val okhttpClient = NetworkFactory.createOkHttpClient(authInterceptor)
        val retrofitClient = RetrofitFactory.createRetrofit("http://192.168.0.11:8123/", okhttpClient)


        return RetrofitFactory.createApi(retrofitClient)
    }
}